package lk.jiat.app.web.websocket;

import jakarta.websocket.Session;
import lk.jiat.app.core.model.Page;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserSessions {
    // userId → (pageName → set of WebSocket sessions)
    private static final Map<Integer, Map<Page, Set<Session>>> userSessions = new ConcurrentHashMap<>();

    /**
     * Adds a user's WebSocket session for a specific page.
     *
     * @param userId the user's unique ID
     * @param page   the page name (e.g. "dashboard", "transferHistory", "scheduledOperations")
     * @param session the WebSocket session
     */
    public static void addUserSession(Integer userId, Page page, Session session) {
        userSessions
                .computeIfAbsent(userId, id -> new ConcurrentHashMap<>())
                .computeIfAbsent(page, p -> ConcurrentHashMap.newKeySet())
                .add(session);
    }

    /**
     * Removes a WebSocket session from all users and page.
     *
     * @param session the WebSocket session to remove
     */
    public static void removeUserSession(Session session) {
        userSessions.values().forEach(pageMap ->
                pageMap.values().forEach(sessionSet -> sessionSet.remove(session))
        );
    }

    public static Integer getUserIdBySessionAndPage(Session session, Page page) {
        for (Map.Entry<Integer, Map<Page, Set<Session>>> userEntry : userSessions.entrySet()) {
            Integer userId = userEntry.getKey();
            Map<Page, Set<Session>> pageMap = userEntry.getValue();

            Set<Session> sessions = pageMap.get(page);
            if (sessions != null && sessions.contains(session)) {
                return userId;
            }
        }
        return null;
    }

    /**
     * Gets all sessions for a user on a specific page.
     *
     * @param userId the user's unique ID
     * @param page   the page name
     * @return a set of sessions, or an empty set if none
     */
    public static Set<Session> getUserSessions(Integer userId, Page page) {
        if (userSessions.containsKey(userId) && userSessions.get(userId).containsKey(page)) {
            return userSessions.get(userId).get(page);
        }
        return Collections.emptySet();
    }

    /**
     * Broadcasts a message to all of a user's sessions on a specific page.
     *
     * @param userId the user's unique ID
     * @param page   the target page
     * @param message the message text to send
     */
    public static void sendMessageToPage(Integer userId, Page page, String message) {
        Set<Session> sessions = getUserSessions(userId, page);
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Broadcasts a message to all sessions of a specific user (across all page).
     *
     * @param userId the user's unique ID
     * @param message the message text to send
     */
    public static void sendMessageToUser(Integer userId, String message) {
        Map<Page, Set<Session>> pageSessions = userSessions.get(userId);
        if (pageSessions != null) {
            pageSessions.values().forEach(sessionSet -> {
                for (Session session : sessionSet) {
                    if (session.isOpen()) {
                        try {
                            session.getBasicRemote().sendText(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    /**
     * Removes all sessions for a given user (optional cleanup helper).
     */
    public static void removeAllUserSessions(Integer userId) {
        userSessions.remove(userId);
    }

    /**
     * Returns a read-only view of all active sessions (for debugging/monitoring).
     */
    public static Map<Integer, Map<Page, Set<Session>>> getAllSessions() {
        return Collections.unmodifiableMap(userSessions);
    }
}
