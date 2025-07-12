package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.Transfer;
import lk.jiat.app.core.service.AccountService;
import lk.jiat.app.core.service.TransactionService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@WebServlet("/loadAllHistory")
public class LoadAllHistory extends HttpServlet {

    @EJB
    private TransactionService transactionService;

    @EJB
    private AccountService accountService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            JSONObject input = new JSONObject(sb.toString());

            String accountNumber = input.getString("accountNumber");
            String status = input.getString("status");

            System.out.println(accountNumber+" "+status);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            List<Transfer> list;

            if(!Objects.equals(accountNumber, "")) {

                if(accountService.getAccount(accountNumber) != null) {

                    Account account = accountService.getAccount(accountNumber);
                    list = transactionService.getTransactions(account);
                }else{
                    resp.setContentType("text/plain");
                    resp.getWriter().write("Account not found");
                    return;
                }

            }else{
                list = transactionService.getAllTransactions();
            }

            JSONArray jsonArray = new JSONArray();

            for(Transfer transfer : list) {

                if(!Objects.equals(status, "") && !transfer.getTransactionStatus().name().equals(status)) {
                    continue;
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("from", transfer.getFromAccount().getAccountNo());
                jsonObject.put("to", transfer.getToAccount().getAccountNo());
                jsonObject.put("amount", transfer.getAmount());
                jsonObject.put("dateTime", formatter.format(transfer.getDateTime()));
                jsonObject.put("status", transfer.getTransactionStatus().name());
                jsonArray.put(jsonObject);
            }

            resp.setContentType("application/json");
            resp.getWriter().write(jsonArray.toString());

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
