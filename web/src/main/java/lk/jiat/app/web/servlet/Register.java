package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.UserService;
import lk.jiat.app.core.util.Encryption;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet("/register")
public class Register extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String mobile = req.getParameter("mobile");
        String amount = req.getParameter("amount");

        System.out.println(name + " " + email + " " + password + " " + confirmPassword + " " + mobile + " " + amount);

        if(mobile.matches("^[0]{1}[7]{1}[01245678]{1}[0-9]{7}$") && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {

            if (userService.getUserByMobile(mobile) == null && userService.getUserByEmail(email) == null) {

                if (password.equals(confirmPassword)) {

                    // Register

                    int number = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);

                    User user = new User(name, mobile, email, Encryption.encrypt(password), null, null);

                    Account account = new Account(String.valueOf(number), Double.valueOf(amount), user, LocalDateTime.now(), null);

                    user.setAccounts(List.of(account));
                    userService.addUser(user);

                    resp.sendRedirect(req.getContextPath() + "/index.jsp");

                } else {
                    req.setAttribute("message", "Passwords do not match");
                    req.getRequestDispatcher("register.jsp").forward(req, resp);
                }
            } else {
                req.setAttribute("message", "Email or Mobile already exists");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
            }

        }else{
            req.setAttribute("message", "Email or Mobile is invalid");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }

    }
}
