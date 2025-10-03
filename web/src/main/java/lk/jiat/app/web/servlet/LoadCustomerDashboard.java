package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.AccountStatus;
import lk.jiat.app.core.model.Transfer;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.TransactionService;
import lk.jiat.app.core.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet("/loadCustomerDashboard")
public class LoadCustomerDashboard extends HttpServlet {

    @EJB
    private UserService userService;

    @EJB
    private TransactionService transactionService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = userService.getUserById(((User) req.getSession().getAttribute("user")).getId());
        Account account = user.getAccounts().get(0);
        Transfer sentTransfer = transactionService.getLatestSent(account);
        Transfer receivedTransfer = transactionService.getLatestReceived(account);

        JSONArray jsonArray = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("balance", "USD "+account.getBalance());
        json.put("suspended", account.getStatus() != AccountStatus.ACTIVE);
        if(sentTransfer != null) {
            json.put("sent", true);
            json.put("sentNumber", sentTransfer.getToAccount().getAccountNo());
            json.put("sentName", sentTransfer.getToAccount().getUser().getName());
            json.put("sentAmount", "USD "+sentTransfer.getAmount());
            json.put("sentDateTime", sentTransfer.getDateTime());
        }else{
            json.put("sent", false);
        }

        if(receivedTransfer != null) {
            json.put("received", true);
            json.put("receivedNumber", receivedTransfer.getFromAccount().getAccountNo());
            json.put("receivedName", receivedTransfer.getFromAccount().getUser().getName());
            json.put("receivedAmount", "USD "+receivedTransfer.getAmount());
            json.put("receivedDateTime", receivedTransfer.getDateTime());
        }else{
            json.put("received", false);
        }



        resp.setContentType("application/json");
        System.out.println(json);
        resp.getWriter().write(json.toString());

    }
}
