package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Objects;

@WebServlet("/transaction")
public class Transaction extends HttpServlet {

    @EJB
    private AccountService accountService;

    @EJB
    private TransactionService transactionService;

    @EJB
    private ScheduledTransactionService scheduledTransactionService;

    @EJB
    private NotificationService notificationService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        boolean instantTransaction = true;

        try {

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            JSONObject input = new JSONObject(sb.toString());

            String toAcc = input.optString("destination",null);
            String amountParam = input.optString("amount",null);
            String date = input.optString("date",null);
            String time = input.optString("time",null);
            HttpSession session = req.getSession(false);
            User fromUser = (session != null) ? (User) session.getAttribute("user") : null;

            if (fromUser == null) {
                resp.getWriter().write("redirect");
                return;
            }

            resp.setContentType("text/plain");

            // Trim and null-check
            if (toAcc == null || toAcc.trim().isEmpty()) {
                resp.getWriter().write("Destination account is invalid.");
                return;
            }


            Double amount;
            try {
                amount = Double.valueOf(amountParam);
                if (amount <= 0) {
                    resp.getWriter().write("Amount must be greater than zero.");
                    return;
                }
            } catch (Exception e) {
                resp.getWriter().write("Invalid amount.");
                return;
            }

            // Check date-time combo logic
            boolean hasDate = date != null && !date.trim().isEmpty();
            boolean hasTime = time != null && !time.trim().isEmpty();

            LocalDateTime dateTime = LocalDateTime.now();

            if ((hasDate && !hasTime) || (!hasDate && hasTime)) {
                resp.getWriter().write("Both date and time must be filled if one is provided.");
                return;
            } else if (hasDate && hasTime) {

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                        .withResolverStyle(ResolverStyle.STRICT);
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                        .withResolverStyle(ResolverStyle.STRICT);

                try {
                    LocalDate formattedDate = LocalDate.parse(date, dateFormatter);
                    LocalTime formattedTime = LocalTime.parse(time, timeFormatter);

                    dateTime = LocalDateTime.of(formattedDate, formattedTime);
                } catch (Exception e) {
                    resp.getWriter().write("Invalid date or time.");
                    return;
                }

                if (dateTime.isBefore(LocalDateTime.now())) {
                    resp.getWriter().write("Date and Time must be after the current date and time.");
                    return;
                }

                instantTransaction = false;
            }

            Account fromAccount = fromUser.getAccounts().get(0);
            Account toAccount = accountService.getAccount(toAcc);

            if (toAccount==null) {
                resp.getWriter().write("Destination account does not exist.");
                return;
            }

            if (Objects.equals(toAccount.getAccountNo(), fromAccount.getAccountNo())) {
                resp.getWriter().write("Your account and destination account are the same.");
                return;
            }

            if (fromAccount.getStatus().equals(AccountStatus.SUSPENDED)) {
                resp.getWriter().write("Your account is suspended.");
                return;
            }

            if (toAccount.getStatus().equals(AccountStatus.SUSPENDED)) {
                resp.getWriter().write("Destination account is suspended.");
                return;
            }

            if (fromAccount.getBalance() < amount) {
                resp.getWriter().write("Insufficient funds.");
                return;
            }

            DecimalFormat df = new DecimalFormat("#0.00");

            User toUser = toAccount.getUser();



            if (!instantTransaction) {

                //scheduled transfer
                scheduledTransactionService.createTransaction(new ScheduledTransfer(dateTime, amount, toAccount, fromAccount));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                Notification notification = new Notification();
                notification.setMessage("$" + df.format(amount) + " has been scheduled to be transferred to " + toAccount.getAccountNo() + " on " + formatter.format(dateTime));
                notification.setUser(fromUser);
                notification.setDateTime(LocalDateTime.now());
                notificationService.sendNotification(notification);

            } else {

                transactionService.createTransaction(new Transfer(dateTime, amount, toAccount, fromAccount));
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                accountService.updateAccount(fromAccount);

                toAccount.setBalance(toAccount.getBalance() + amount);
                accountService.updateAccount(toAccount);

                Notification notification = new Notification();
                notification.setMessage("$" + df.format(amount) + " has been transferred to " + toAccount.getAccountNo());
                notification.setUser(fromUser);
                notification.setDateTime(LocalDateTime.now());
                notificationService.sendNotification(notification);

                notification.setMessage("$" + df.format(amount) + " has been transferred to you by " + fromAccount.getAccountNo());
                notification.setUser(toUser);
                notificationService.sendNotification(notification);

            }

            resp.getWriter().write("success");
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
