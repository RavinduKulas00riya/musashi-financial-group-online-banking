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
import lk.jiat.app.core.service.UserService;

import java.io.IOException;

@WebServlet("/admin_login")
public class AdminLogin extends HttpServlet {

    @EJB
    private UserService userService;

    @Inject
    private SecurityContext securityContext;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        System.out.println("AdminLogin doGet");
//        User user = new User("John Doe", "123", "email", Encryption.encrypt("123"), new ArrayList<>(), new ArrayList<>());
//        userService.addUser(user);

        String emailOrMobile = req.getParameter("emailOrMobile");
        String password = req.getParameter("password");

//        User user = userService.validate(emailOrMobile, password);
//
//        if(user != null) {

            AuthenticationParameters parameters = AuthenticationParameters.withParams()
                    .credential(new UsernamePasswordCredential(emailOrMobile, password));

            AuthenticationStatus status = securityContext.authenticate(req, resp, parameters);

            if (status == AuthenticationStatus.SUCCESS) {

                req.getSession().setAttribute("admin", userService.getUserByEmail(emailOrMobile));
                resp.sendRedirect(req.getContextPath() + "/admin/home.jsp");

//                if(user.getUserType().equals(UserType.CUSTOMER)) {
//                    System.out.println("Customer logged in");
//                    resp.sendRedirect(req.getContextPath() + "/customer/home.jsp");
//                }else{
//                    System.out.println("Admin logged in");
//                    resp.sendRedirect(req.getContextPath() + "/admin/home.jsp");
//                }

            } else {
                System.out.println("Authentication failed");
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//            throw new LoginFailedException("Invalid email or password");
            }
//        }else{
//            System.out.println("User not found");
//            resp.getWriter().write("Invalid email or password");
//        }

//        resp.setContentType("text/html");


//
//            req.getSession().setAttribute("user", user);
//
//            if(user.getUserType().equals(UserType.CUSTOMER)){
//
//                resp.sendRedirect(req.getContextPath() + "/customer/home.jsp");
//
//            }else if(user.getUserType().equals(UserType.ADMIN)){
//
//                resp.sendRedirect(req.getContextPath() + "/admin/home.jsp");
//            }
//
//        }else{
//
////            resp.getWriter().write("Invalid email or password");
//            System.out.println("Invalid email or password");
//        }

    }
}
