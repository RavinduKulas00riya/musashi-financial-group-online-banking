package lk.jiat.app.web.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import lk.jiat.app.core.event.PublicEvent;
import lk.jiat.app.core.model.*;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(
        value = "/customerTransactionHistory",
        configurator = CustomerTransactionHistory.Configurator.class
)
@ApplicationScoped
public class CustomerTransactionHistory {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");

        if (httpSession != null) {
            Integer id = (Integer) httpSession.getAttribute("user_id");
            if (id != null) {
                UserSessions.addUserSession(id, Page.CUSTOMER_HISTORY, session);
                System.out.println("TH WebSocket opened for user ID: " + id);
                sendToUser(id, "");
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        Integer id = UserSessions.getUserIdBySessionAndPage(session, Page.CUSTOMER_HISTORY);
        UserSessions.removeSessionFromPage(id, Page.CUSTOMER_HISTORY, session);
        System.out.println("User id: " + id + " removed from customer history");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, Session session, EndpointConfig config) {
        try {
            // Get user ID from the session map
            Integer userId = UserSessions.getUserIdBySessionAndPage(session, Page.CUSTOMER_HISTORY);
            System.out.println("Message received: " + message);
            if (userId != null) {
                sendToUser(userId, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCustomerTransactionHistoryUpdate(@Observes PublicEvent event) {

        Set<Session> sessions = UserSessions.getUserSessions(event.getUserId(), Page.CUSTOMER_HISTORY);
        sessions.forEach(session -> {
            if (session != null && session.isOpen()) {

                sendToUser(event.getUserId(), "");

            }
        });

    }

    public void sendToUser(Integer id, String message) {

        JSONObject response = new JSONObject();

        Set<Session> sessions = UserSessions.getUserSessions(id, Page.CUSTOMER_HISTORY);

        if (sessions.isEmpty()) {
            return;
        }

        try {

            if (message.isEmpty()) {
                response.put("task", "update");
                sendData(sessions, response.toString());
                return;
            }

            JSONObject filters = new JSONObject(message);
            String accountNumber = filters.getString("accountNum");
            String counterparty = filters.getString("counterparty");
            String startDate = filters.getString("startDate");
            String endDate = filters.getString("endDate");
            String type = filters.getString("type");

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalDate formattedStartDate;
            LocalDate formattedEndDate;

            if (!startDate.isEmpty()) {
                try {
                    formattedStartDate = LocalDate.parse(startDate, dateFormatter);
                } catch (Exception e) {
                    response.put("task", "invalidStartDate");
                    sendData(sessions, response.toString());
                    return;
                }
            }

            if (!endDate.isEmpty()) {
                try {
                    formattedEndDate = LocalDate.parse(endDate, dateFormatter);
                } catch (Exception e) {
                    response.put("task", "invalidEndDate");
                    sendData(sessions, response.toString());
                    return;
                }
            }

            Context ctx = new InitialContext();
            UserService userService = (UserService) ctx.lookup("java:global/musashi-banking-system-ear/ejb-module/UserSessionBean");
            TransactionService transactionService = (TransactionService) ctx.lookup("java:global/musashi-banking-system-ear/ejb-module/TransactionSessionBean");
            User user = userService.getUserById(id);
            Account account = user.getAccounts().get(0);

            JSONArray rows = new JSONArray();

            //load all
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


    // === Inner class to link HttpSession ===
    public static class Configurator extends ServerEndpointConfig.Configurator {
        @Override
        public void modifyHandshake(ServerEndpointConfig config,
                                    HandshakeRequest request,
                                    HandshakeResponse response) {
            HttpSession httpSession = (HttpSession) request.getHttpSession();
            if (httpSession != null) {
                config.getUserProperties().put("httpSession", httpSession);
            }
        }
    }
}
