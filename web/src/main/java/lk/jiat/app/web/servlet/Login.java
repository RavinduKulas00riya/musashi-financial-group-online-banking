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

import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {

    @EJB
    private UserService userService;

    @Inject
    private SecurityContext securityContext;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        System.out.println("Login email: " + email);

        if(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {

            AuthenticationParameters parameters = AuthenticationParameters.withParams()
                    .credential(new UsernamePasswordCredential(email, password));

            AuthenticationStatus status = securityContext.authenticate(req, resp, parameters);

//            User user = userService.validate(email, Encryption.encrypt(password));

            if (status == AuthenticationStatus.SUCCESS) {

                User user = userService.getUserByEmail(email);

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
