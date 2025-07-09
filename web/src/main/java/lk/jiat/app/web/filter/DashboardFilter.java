package lk.jiat.app.web.filter;

import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.jiat.app.core.model.Notification;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.UserService;

import java.io.IOException;
import java.util.List;

@WebFilter("/customer/home.jsp")
public class DashboardFilter implements Filter {

    @EJB
    private UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            User sessionUser = (User) session.getAttribute("user");

            // Get up-to-date user with notifications
            User fullUser = userService.getUserById(sessionUser.getId());

            if (fullUser != null && fullUser.getNotifications() != null) {
                List<Notification> notifications = fullUser.getNotifications();
                // Sort by newest first
                notifications.sort((n1, n2) -> n2.getDateTime().compareTo(n1.getDateTime()));
                request.setAttribute("notifications", notifications);
            }

            chain.doFilter(request, response); // proceed to JSP
        } else {
            System.out.println("session is null or user not logged in");
            res.sendRedirect(req.getContextPath() + "/index.jsp"); // redirect to login page
        }
    }

}
