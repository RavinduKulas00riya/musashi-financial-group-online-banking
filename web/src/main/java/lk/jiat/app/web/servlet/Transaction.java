package lk.jiat.app.web.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.AccountService;
import lk.jiat.app.core.service.NotificationService;
import lk.jiat.app.core.service.TransactionService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@WebServlet("/transaction")
public class Transaction extends HttpServlet {

    @EJB
    private AccountService accountService;

    @EJB
    private TransactionService transactionService;

    @EJB
    private NotificationService notificationService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String toAcc = req.getParameter("destination");
        String amountParam = req.getParameter("amount");
        String date = req.getParameter("date");
        String time = req.getParameter("time");

        String message = null;

        // Trim and null-check
        if (toAcc == null || toAcc.trim().isEmpty()) {
            message = "Destination account is required.";
        }

        Double amount = null;
        try {
            amount = Double.valueOf(amountParam);
            if (amount <= 0) {
                message = "Amount must be greater than zero.";
            }
        } catch (Exception e) {
            message = "Invalid amount.";
        }

        // Check date-time combo logic
        boolean hasDate = date != null && !date.trim().isEmpty();
        boolean hasTime = time != null && !time.trim().isEmpty();

        LocalDateTime dateTime = LocalDateTime.now();
        TransactionStatus transactionStatus = TransactionStatus.COMPLETED;

        if ((hasDate && !hasTime) || (!hasDate && hasTime)) {
            message = "Both date and time must be filled if one is provided.";
        }else if (hasDate && hasTime) {
            dateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));

            if(dateTime.isBefore(LocalDateTime.now())) {
                message = "Invalid date or time.";
            }

            transactionStatus = TransactionStatus.PENDING;
        }

        if (message != null) {
            req.setAttribute("message", message);
            req.getRequestDispatcher("customer/home.jsp").forward(req, resp);
            return;
        }

        //instant
        User fromUser = (User) req.getSession().getAttribute("user");
        Account fromAccount = accountService.getAccount(fromUser.getAccounts().get(0).getAccountNo());
        Account toAccount = accountService.getAccount(toAcc);

        if (fromAccount == null ) {
            req.setAttribute("message", "Your account is suspended or has been removed.");
            req.getRequestDispatcher("customer/home.jsp").forward(req, resp);
            return;
        }

        if (toAccount == null) {
            req.setAttribute("message", "Destination account is suspended or has been removed.");
            req.getRequestDispatcher("customer/home.jsp").forward(req, resp);
            return;
        }

        User toUser = toAccount.getUser();

        transactionService.createTransaction(new Transfer(dateTime,transactionStatus,amount,toAccount,fromAccount));

        Notification notification = new Notification();
        notification.setMessage("$"+amount+" has been transferred to "+toAccount.getAccountNo());
        notification.setUser(fromUser);
        notification.setDateTime(dateTime);
        notificationService.sendNotification(notification);

        notification.setMessage("$"+amount+" has been transferred to you by "+fromAccount.getAccountNo());
        notification.setUser(toUser);
        notificationService.sendNotification(notification);

        resp.sendRedirect(req.getContextPath()+"/customer/home.jsp");

    }
}
