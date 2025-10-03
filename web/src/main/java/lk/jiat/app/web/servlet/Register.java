package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.UserService;
import lk.jiat.app.core.util.Encryption;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet("/register")
public class Register extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            HttpSession session = req.getSession();

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            JSONObject input = new JSONObject(sb.toString());

            if (session.getAttribute("temp_user") == null) {

                String name = input.getString("name");
                String email = input.getString("email");
                String mobile = input.getString("mobile");
                String amount = input.getString("amount");

                if(Objects.equals(name, "")){
                    resp.getWriter().write("Name Cannot Be Empty");
                }else if(Objects.equals(email, "")){
                    resp.getWriter().write("Email Cannot Be Empty");
                }else if(Objects.equals(mobile, "")){
                    resp.getWriter().write("Mobile number Cannot Be Empty");
                }else if(Objects.equals(amount, "")){
                    resp.getWriter().write("Amount Cannot Be Empty");
                }else {

                    if (mobile.matches("^[0]{1}[7]{1}[01245678]{1}[0-9]{7}$") && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {

                        if (userService.getUserByMobile(mobile) == null && userService.getUserByEmail(email) == null) {

                            User user = new User();
                            user.setName(name);
                            user.setEmail(email);
                            user.setMobile(mobile);

                            int number = ThreadLocalRandom.current().nextInt(1_000_000, 10_000_000);
                            Account account = new Account(String.valueOf(number), Double.valueOf(amount), user, LocalDateTime.now(), null);
                            user.setAccounts(List.of(account));
                            session.setAttribute("temp_user", user);

                            resp.getWriter().write("success");
                        } else {
                            resp.getWriter().write("Email or Mobile Number Already in Use");
                        }

                    } else {
                        resp.getWriter().write("Invalid Email or Mobile Number");
                    }
                }

            } else {

                User user = (User) session.getAttribute("temp_user");

                System.out.println(user.getEmail());

                String password = input.getString("password");
                String confirmPassword = input.getString("confirmPassword");

                if (Objects.equals(password, "")) {

                    resp.getWriter().write("Password Cannot Be Empty");

                } else if (password.equals(confirmPassword)) {

                    user.setPassword(Encryption.encrypt(password));
                    userService.addUser(user);

                    resp.getWriter().write("done");

                } else {
                    resp.getWriter().write("Passwords Do Not Match");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
