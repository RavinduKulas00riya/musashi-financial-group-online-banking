package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.NotificationService;
import lk.jiat.app.core.service.UserService;

import java.io.IOException;

@WebServlet("/updateNotifications")
public class UpdateNotifications extends HttpServlet {

    @EJB
    private NotificationService notificationService;

    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            User sessionUser = (User) req.getSession().getAttribute("user");
            User user = userService.getUserById(sessionUser.getId());

            notificationService.markAllAsSeen(user);
            resp.getWriter().write("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
