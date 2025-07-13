package lk.jiat.app.web.servlet;

import java.io.IOException;
import java.io.BufferedReader;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.AccountService;
import lk.jiat.app.core.service.InterestService;
import lk.jiat.app.core.service.TransactionService;
import org.json.JSONObject;
import org.json.JSONArray;

@WebServlet("/loadHistory")
public class LoadHistory extends HttpServlet {

    @EJB
    private AccountService accountService;

    @EJB
    private TransactionService transactionService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            User user = (User) request.getSession().getAttribute("user");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            JSONObject input = new JSONObject(sb.toString());

            String accountNo = input.getString("accountNumber");
            String name = input.getString("name");
            String status = input.getString("status");
            String sentOrReceived;

            if(status.equals(TransactionStatus.COMPLETED.name())){
                sentOrReceived = input.getString("sentOrReceived");
            }else{
                sentOrReceived = null;
            }

            JSONArray result = new JSONArray();

//            List<Transfer> transactions = transactionService.getTransactions(user.getAccounts().get(0));
//
//            if (!Objects.equals(accountNo, "") && accountService.getAccount(accountNo) == null) {
//                sendError(response, "Account Number is invalid or suspended");
//                return;
//            }else{
//                transactions = transactionService.getTransactions(user.getAccounts().get(0));
//            }

            List<Transfer> transactions;

            if (!Objects.equals(accountNo, "")){

                Account account = accountService.getAccount(accountNo);

                if(account.getStatus().equals(AccountStatus.INACTIVE)){
                    sendError(response, "Account Number is invalid or suspended");
                    return;
                }else{
                    transactions = transactionService.getTransactions(user.getAccounts().get(0), account);
                }
            }else{
                transactions = transactionService.getTransactions(user.getAccounts().get(0));
            }

            if (transactions.isEmpty()) {
                sendError(response, "No transactions found");
                return;
            }

            transactions.forEach(transaction -> {
                JSONObject json = new JSONObject();

                if (!transaction.getTransactionStatus().name().equals(status)) {
                    return;
                }

                if (transaction.getFromAccount().getAccountNo().equals(user.getAccounts().get(0).getAccountNo())) {
                    json.put("accountNumber", transaction.getToAccount().getAccountNo());
                    json.put("name", transaction.getToAccount().getUser().getName());
                    json.put("transactionType", "Sent");
                } else {
                    json.put("accountNumber", transaction.getFromAccount().getAccountNo());
                    json.put("name", transaction.getFromAccount().getUser().getName());
                    json.put("transactionType", "Received");
                }

                if (!name.isEmpty() && !json.getString("name").contains(name)) {
                    return;
                }

                json.put("amount", transaction.getAmount());
                json.put("dateTime", formatter.format(transaction.getDateTime()));

                if(status.equals(TransactionStatus.PENDING.name())) {

                    if(json.getString("transactionType").equals("Received")){
                        return;
                    }

                    System.out.println(user.getAccounts().get(0).getAccountNo()+" pending transaction");

                    json.put("transactionId", transaction.getId());

                    System.out.println(json.getString("transactionType"));

                    if(json.getString("transactionType").equals("Received")){
                        return;
                    }

                }else{
                    if (!Objects.equals(sentOrReceived, "") && !json.getString("transactionType").equals(sentOrReceived)) {
                        return;
                    }
                }

                result.put(json);
            });

            if(result.isEmpty()){
                sendError(response, "No transactions found");
                return;
            }

            response.setContentType("application/json");
            response.getWriter().write(result.toString());

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void sendError(HttpServletResponse response, String message){

        try {
            response.setContentType("text/plain");
            response.getWriter().write(message);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}