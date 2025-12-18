package lk.jiat.app.web.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import lk.jiat.app.core.dto.CustomerTimelyOperationsTableDTO;
import lk.jiat.app.core.dto.CustomerTransactionHistoryTableDTO;
import lk.jiat.app.core.event.PublicEvent;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.ScheduledTransactionService;
import lk.jiat.app.core.service.TransactionService;
import lk.jiat.app.core.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Set;

@ServerEndpoint(
        value = "/customerTimelyOperations",
        configurator = Configurator.class
)
@ApplicationScoped
public class CustomerTimelyOperations {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");

        if (httpSession != null) {
            Integer id = (Integer) httpSession.getAttribute("user_id");
            if (id != null) {
                UserSessions.addUserSession(id, Page.CUSTOMER_SCHEDULE, session);
                System.out.println("TO WebSocket opened for user ID: " + id);
                sendToUser(id, "filters");
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        Integer id = UserSessions.getUserIdBySessionAndPage(session, Page.CUSTOMER_SCHEDULE);
        UserSessions.removeSessionFromPage(id, Page.CUSTOMER_SCHEDULE, session);
        System.out.println("User id: " + id + " removed from customer schedule");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, Session session, EndpointConfig config) {
        try {
            // Get user ID from the session map
            Integer userId = UserSessions.getUserIdBySessionAndPage(session, Page.CUSTOMER_SCHEDULE);
            System.out.println("Message received: " + message);
            if (userId != null) {
                sendToUser(userId, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCustomerTimelyOperationsUpdate(@Observes(during = TransactionPhase.AFTER_SUCCESS) PublicEvent event) {

        Set<Session> sessions = UserSessions.getUserSessions(event.getUserId(), Page.CUSTOMER_SCHEDULE);

        sessions.forEach(session -> {
            if (session != null && session.isOpen()) {

                sendToUser(event.getUserId(), "");

            }
        });

    }

    public void sendToUser(Integer id, String message) {

        Set<Session> sessions = UserSessions.getUserSessions(id, Page.CUSTOMER_SCHEDULE);

        DateTimeFormatter convertToDate =  DateTimeFormatter.ofPattern("MMM dd, yyyy");
        DateTimeFormatter convertToTime = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
//        DateTimeFormatter convertToTimeWithSeconds = DateTimeFormatter.ofPattern("HH:mm:ss");
        DecimalFormat df = new DecimalFormat("#0.00");

        JSONObject response = new JSONObject();

        if (sessions.isEmpty()) {
            return;
        }

        try {

            //notify user that there are new records
            if (message.isEmpty()) {
                response.put("task", "update");
                sendData(sessions, response.toString());
                return;
            }

            //asking for filters
            if (message.equals("filters")) {
                response.put("task", "filters");
                sendData(sessions, response.toString());
                return;
            }

            JSONObject filters = new JSONObject(message);

            String scheduledStart = filters.getString("scheduledStart");
            String scheduledEnd = filters.getString("scheduledEnd");
            String createdStart = filters.getString("createdStart");
            String createdEnd = filters.getString("createdEnd");
            String accountNumber = filters.getString("accountNum");
            String counterparty = filters.getString("counterparty");
            String sortBy = filters.getString("sortBy");
            String stringStatus = filters.getString("status");
            Integer page = filters.getInt("page");

            LocalDate scheduledStartDate = null;
            LocalDate scheduledEndDate = null;
            LocalDate createdStartDate = null;
            LocalDate createdEndDate = null;

            try {
                scheduledStartDate = LocalDate.parse(scheduledStart, dateFormatter);
            } catch (Exception ignored) {}

            try {
                scheduledEndDate = LocalDate.parse(scheduledEnd, dateFormatter);
            }catch (Exception ignored) {}

            try {
                createdStartDate = LocalDate.parse(createdStart, dateFormatter);
            }catch (Exception ignored) {}

            try {
                createdEndDate = LocalDate.parse(createdEnd, dateFormatter);
            }catch (Exception ignored) {}

            ScheduledTransactionStatus status = null;

            if(stringStatus == "Pending") {
                status = ScheduledTransactionStatus.PENDING;
            }else if (stringStatus == "Paused") {
                status = ScheduledTransactionStatus.PAUSED;
            }

            response.put("task", "loadRows");

            Context ctx = new InitialContext();
            UserService userService = (UserService) ctx.lookup("java:global/musashi-banking-system-ear/ejb-module/UserSessionBean");
            ScheduledTransactionService scheduledTransactionService = (ScheduledTransactionService) ctx.lookup("java:global/musashi-banking-system-ear/ejb-module/ScheduledTransactionSessionBean");
            User user = userService.getUserById(id);
            Account account = user.getAccounts().get(0);

            JSONArray rows = new JSONArray();

            CustomerTimelyOperationsTableDTO dto = scheduledTransactionService.customerTimelyOperationsTable(account, scheduledStartDate, scheduledEndDate, createdStartDate, createdEndDate, sortBy, page, 5, accountNumber, counterparty, status);
            List<ScheduledTransfer> transactionList = dto.getList();
            long totalRows = dto.getTotalRowCountAfterFiltering();

            for (ScheduledTransfer transaction : transactionList) {

                JSONObject row = new JSONObject();

                row.put("scheduledDate", convertToDate.format(transaction.getDateTime()));
                row.put("scheduledTime", convertToTime.format(transaction.getDateTime()));
                row.put("createdDate", convertToDate.format(transaction.getCreated_datetime()));
                row.put("createdTime", convertToTime.format(transaction.getCreated_datetime()));
                row.put("amount", df.format(transaction.getAmount()));
                row.put("counterparty", account.getUser().getName());
                row.put("accountNum", transaction.getToAccount().getAccountNo());
                row.put("status", transaction.getStatus().name().toLowerCase());

                rows.put(row);

            }

            response.put("rows", rows);
            response.put("totalAmount", df.format(dto.getTotalAmount()));
            response.put("resultCount", dto.getTotalRowCount());
            LocalDateTime nearestTransactionDateTime = dto.getNextDateTime();
            response.put("nextDate", convertToDate.format(nearestTransactionDateTime));
            response.put("nextTime", convertToTime.format(nearestTransactionDateTime));

            int totalPages = (int) Math.ceil((double) totalRows / 7);
            response.put("totalPages", totalPages);
            response.put("currentPage", page);

            sendData(sessions, response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendData(Set<Session> sessions, String message) {

        for (Session session : sessions) {

            if (session != null && session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
