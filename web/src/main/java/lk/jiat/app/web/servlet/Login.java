package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.model.UserType;
import lk.jiat.app.core.service.UserService;

import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String emailOrMobile = req.getParameter("emailOrMobile");
        String password = req.getParameter("password");

        User user = userService.validate(emailOrMobile, password);

        if(user != null && user.getUserType().equals(UserType.CUSTOMER)) {
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/customer/home.jsp");
        }else{
            resp.setContentType("text/html");
            resp.getWriter().write("Invalid email/mobile or password");
        }
    }
}
