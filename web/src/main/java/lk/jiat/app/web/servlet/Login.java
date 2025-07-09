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
import lk.jiat.app.core.util.Encryption;

import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        System.out.println("Login email: " + email);

        if(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {

            User user = userService.validate(email, Encryption.encrypt(password));

            if (user != null && user.getUserType().equals(UserType.CUSTOMER)) {
                req.getSession().setAttribute("user_id", user.getId());
                req.getSession().setMaxInactiveInterval(15 * 60);
                resp.sendRedirect(req.getContextPath() + "/customer/home.jsp");
            } else {
                req.setAttribute("message", "Incorrect email or password");
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        }else{
            req.setAttribute("message", "Invalid email or password");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}
