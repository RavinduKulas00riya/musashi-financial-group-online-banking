package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.TransactionService;
import lk.jiat.app.core.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

@WebServlet("/loadCustomerDashboard")
public class LoadCustomerDashboard extends HttpServlet {

    @EJB
    private UserService userService;

    @EJB
    private TransactionService transactionService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            User user = userService.getUserById(((User) req.getSession().getAttribute("user")).getId());
            Account account = user.getAccounts().get(0);
            Transfer sentTransfer = transactionService.getLatestSent(account);
            Transfer receivedTransfer = transactionService.getLatestReceived(account);

            JSONObject json = new JSONObject();
            DecimalFormat df = new DecimalFormat("#0.00");
            String balanceFormatted = df.format(account.getBalance());
            System.out.println("balanceFormatted: " + balanceFormatted);
            json.put("balance", "USD " + balanceFormatted);
            json.put("suspended", account.getStatus() != AccountStatus.ACTIVE);
            if (sentTransfer != null) {
                json.put("sent", true);
                json.put("sentNumber", sentTransfer.getToAccount().getAccountNo());
                json.put("sentName", sentTransfer.getToAccount().getUser().getName());
                balanceFormatted = df.format(sentTransfer.getAmount());
                System.out.println("balanceFormatted: " + balanceFormatted);
                json.put("sentAmount", "USD " + balanceFormatted);
                json.put("sentDateTime", sentTransfer.getDateTime());
            } else {
                json.put("sent", false);
            }

            if (receivedTransfer != null) {
                json.put("received", true);
                json.put("receivedNumber", receivedTransfer.getFromAccount().getAccountNo());
                json.put("receivedName", receivedTransfer.getFromAccount().getUser().getName());
                balanceFormatted = df.format(receivedTransfer.getAmount());
                System.out.println("balanceFormatted: " + balanceFormatted);
                json.put("receivedAmount", "USD " + balanceFormatted);
                json.put("receivedDateTime", receivedTransfer.getDateTime());
            } else {
                json.put("received", false);
            }

            resp.setContentType("application/json");
            resp.getWriter().write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
