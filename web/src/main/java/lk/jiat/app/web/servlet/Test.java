package lk.jiat.app.web.servlet;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.UserTransaction;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.UserService;
import lk.jiat.app.core.util.Encryption;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/test")
public class Test extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        System.out.println("Test doGet");
//        User user = new User("John Doe", "123", "email", Encryption.encrypt("123"), new ArrayList<>(), new ArrayList<>());
//        userService.addUser(user);

        String emailOrMobile = req.getParameter("emailOrMobile");
        String password = req.getParameter("password");

        resp.setContentType("text/html");

        if(userService.validate(emailOrMobile,password)){
            resp.getWriter().println("success");
        }else{
            resp.getWriter().println("fail");
        }
    }
}
