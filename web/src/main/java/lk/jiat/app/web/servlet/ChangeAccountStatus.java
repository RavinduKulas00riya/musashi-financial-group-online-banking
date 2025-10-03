package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.AccountStatus;
import lk.jiat.app.core.service.AccountService;

import java.io.IOException;

@WebServlet("/changeAccountStatus")
public class ChangeAccountStatus extends HttpServlet {

    @EJB
    private AccountService accountService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        resp.setContentType("text/html");
        try {

            Account account = accountService.getAccount(id);
            if (account != null) {

                if (account.getStatus().equals(AccountStatus.ACTIVE)) {
                    account.setStatus(AccountStatus.SUSPENDED);
                } else {
                    account.setStatus(AccountStatus.ACTIVE);
                }

                accountService.updateAccount(account);
                resp.getWriter().write("success");
            }else{
                resp.getWriter().write("Account not found");
            }
        } catch (Exception e) {
            System.out.println(e);
            resp.getWriter().write("Server Error");
        }
    }
}
