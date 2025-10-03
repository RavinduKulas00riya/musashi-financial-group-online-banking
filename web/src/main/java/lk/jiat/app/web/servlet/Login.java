package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.model.UserType;
import lk.jiat.app.core.service.UserService;
import lk.jiat.app.core.util.Encryption;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

@WebServlet("/login")
public class Login extends HttpServlet {

    @EJB
    private UserService userService;

    @Inject
    private SecurityContext securityContext;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        JSONObject input = new JSONObject(sb.toString());

        String email = input.getString("email");
        String password = input.getString("password");

        if(Objects.equals(email, "")){
            resp.getWriter().write("Email Cannot Be Empty");
            return;
        }

        if(Objects.equals(password, "")){
            resp.getWriter().write("Password Cannot Be Empty");
            return;
        }

        if (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {

            AuthenticationParameters parameters = AuthenticationParameters.withParams()
                    .credential(new UsernamePasswordCredential(email, password));

            AuthenticationStatus status = securityContext.authenticate(req, resp, parameters);

            if (status == AuthenticationStatus.SUCCESS) {

                User user = userService.getUserByEmail(email);

                req.getSession().setAttribute("user_id", user.getId());
                req.getSession().setMaxInactiveInterval(15 * 60);
                resp.getWriter().write("success");
            }
        }else{
            resp.getWriter().write("Invalid Email or Password");
        }


    }
}
