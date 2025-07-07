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
import java.util.Objects;

@WebServlet("/login")
public class Login extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        System.out.println("Login doGet");
//        User user = new User("John Doe", "123", "email", Encryption.encrypt("123"), new ArrayList<>(), new ArrayList<>());
//        userService.addUser(user);

        String emailOrMobile = req.getParameter("emailOrMobile");
        String password = req.getParameter("password");
        String userType = req.getParameter("userType");

        resp.setContentType("text/html");

        User user = userService.validate(emailOrMobile, password);

        if(user != null) {
            if(user.getUserType().equals(UserType.ADMIN) && Objects.equals(userType, "ADMIN")) {
                // admin
                resp.getWriter().write("hello admin");
                return;
            }

            if(user.getUserType().equals(UserType.CUSTOMER) && Objects.equals(userType, "CUSTOMER")) {
                // customer
                resp.getWriter().write("hello customer");
                return;
            }
        }

        resp.getWriter().write("Invalid email or password");
    }
}
