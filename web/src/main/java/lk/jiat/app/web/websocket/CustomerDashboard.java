package lk.jiat.app.web.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import lk.jiat.app.core.event.PublicEvent;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.TransactionService;
import lk.jiat.app.core.service.UserService;
import org.json.JSONObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(
        value = "/customerDashboard",
        configurator = CustomerDashboard.Configurator.class
)
@ApplicationScoped
public class CustomerDashboard {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");

        if (httpSession != null) {
            Integer id = (Integer) httpSession.getAttribute("user_id");
            if (id != null) {
                UserSessions.addUserSession(id, Page.CUSTOMER_DASHBOARD, session);
                System.out.println("D WebSocket opened for user ID: " + id);
                sendToUser(id);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        Integer id = UserSessions.getUserIdBySessionAndPage(session, Page.CUSTOMER_DASHBOARD);
        UserSessions.removeSessionFromPage(id,Page.CUSTOMER_DASHBOARD, session);
        System.out.println("User id: " + id+" removed from customer dashboard");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, Session session, EndpointConfig config) {
        try {
            // Get user ID from the session map
            Integer userId = UserSessions.getUserIdBySessionAndPage(session, Page.CUSTOMER_DASHBOARD);

            if (userId != null) {
                sendToUser(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCustomerDashboardUpdate(@Observes(during = TransactionPhase.AFTER_SUCCESS) PublicEvent event) {
        sendToUser(event.getUserId());
    }

    // Sends a message only to a specific user
    public static void sendToUser(Integer id) {
        Set<Session> sessions = UserSessions.getUserSessions(id, Page.CUSTOMER_DASHBOARD);

        if(sessions.isEmpty()) {
            return;
        }

        System.out.println("Sessions: " + sessions.size());

        try {
            Context ctx = new InitialContext();
            UserService userService = (UserService) ctx.lookup("java:global/musashi-banking-system-ear/ejb-module/UserSessionBean");
            TransactionService transactionService = (TransactionService) ctx.lookup("java:global/musashi-banking-system-ear/ejb-module/TransactionSessionBean");
            User user = userService.getUserById(id);
            Account account = user.getAccounts().get(0);
            Transfer sentTransfer = transactionService.getLatestSent(account);
            Transfer receivedTransfer = transactionService.getLatestReceived(account);

            JSONObject json = new JSONObject();
            json.put("task", "loadDashboard");
            DecimalFormat df = new DecimalFormat("#0.00");
            String balanceFormatted = df.format(account.getBalance());
            json.put("balance", "USD " + balanceFormatted);
            json.put("suspended", account.getStatus() != AccountStatus.ACTIVE);
            if (sentTransfer != null) {
                json.put("sent", true);
                json.put("sentNumber", sentTransfer.getToAccount().getAccountNo());
                json.put("sentName", sentTransfer.getToAccount().getUser().getName());
                balanceFormatted = df.format(sentTransfer.getAmount());
                json.put("sentAmount", "USD " + balanceFormatted);
                json.put("sentDateTime", sentTransfer.getDateTime());
            } else {
                json.put("sent", false);
            }

            if (receivedTransfer != null) {
                json.put("received", true);
                json.put("receivedNumber", receivedTransfer.getFromAccount().getAccountNo());
                json.put("receivedName", receivedTransfer.getFromAccount().getUser().getName());
                balanceFormatted = df.format(receivedTransfer.getAmount());
                json.put("receivedAmount", "USD " + balanceFormatted);
                json.put("receivedDateTime", receivedTransfer.getDateTime());
            } else {
                json.put("received", false);
            }

            sessions.forEach(session -> {

                if (session != null && session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(json.toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
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
