package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.Notification;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet("/loadNotifications")
public class LoadNotifications extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            User user = userService.getUserById(((User) req.getSession().getAttribute("user")).getId());

            JSONObject json = new JSONObject();
            JSONArray notificationsArray = new JSONArray();
            if (user.getNotifications() != null) {
                for (Notification n : user.getNotifications()) {
                    JSONObject notifJson = new JSONObject();
                    notifJson.put("id", n.getId());
                    notifJson.put("message", n.getMessage());
                    notifJson.put("status", n.getStatus().toString());
                    notifJson.put("dateTime", n.getDateTime().toString());
                    notificationsArray.put(notifJson);
                }
            }
            json.put("notifications", notificationsArray);

            resp.setContentType("application/json");
            resp.getWriter().write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
