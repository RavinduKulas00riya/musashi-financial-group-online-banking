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
import lk.jiat.app.core.service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/transaction")
public class Transaction extends HttpServlet {

    @EJB
    private AccountService accountService;

    @EJB
    private TransactionService transactionService;

    @EJB
    private NotificationService notificationService;

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User fromUser = (User) req.getSession().getAttribute("user");
        setNotifications(req, fromUser);

        String destinationAccountNo = req.getParameter("destination");
        String amountParam = req.getParameter("amount");
        String date = req.getParameter("date");
        String time = req.getParameter("time");

        String errorMessage = validateInput(destinationAccountNo, amountParam, date, time);
        if (errorMessage != null) {
            forwardWithError(req, resp, errorMessage);
            return;
        }

        Double amount = Double.parseDouble(amountParam);
        LocalDateTime transactionDateTime = determineTransactionDateTime(date, time);
        TransactionStatus transactionStatus = transactionDateTime.isAfter(LocalDateTime.now())
                ? TransactionStatus.PENDING
                : TransactionStatus.COMPLETED;

        Account fromAccount = getFromAccount(fromUser);
        Account toAccount = accountService.getAccount(destinationAccountNo);

        errorMessage = validateAccountsAndBalance(fromAccount, toAccount, amount);
        if (errorMessage != null) {
            forwardWithError(req, resp, errorMessage);
            return;
        }

        processTransaction(fromAccount, toAccount, amount, transactionDateTime, transactionStatus);
        sendNotifications(fromAccount, toAccount, amount, transactionDateTime, transactionStatus);

        resp.sendRedirect(req.getContextPath() + "/customer/home.jsp");
    }

    private void setNotifications(HttpServletRequest req, User user) {
        if (user != null && user.getNotifications() != null) {
            List<Notification> notifications = user.getNotifications();
            notifications.sort((n1, n2) -> n2.getDateTime().compareTo(n1.getDateTime()));
            req.setAttribute("notifications", notifications);
        }
    }

    private String validateInput(String destination, String amountParam, String date, String time) {
        if (isEmpty(destination)) {
            return "Destination account is required.";
        }
        try {
            double amount = Double.parseDouble(amountParam);
            if (amount <= 0) {
                return "Amount must be greater than zero.";
            }
        } catch (NumberFormatException e) {
            return "Invalid amount.";
        }
        boolean hasDate = !isEmpty(date);
        boolean hasTime = !isEmpty(time);
        if (hasDate != hasTime) {
            return "Both date and time must be provided together.";
        }
        if (hasDate && hasTime) {
            try {
                LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
                if (dateTime.isBefore(LocalDateTime.now())) {
                    return "Invalid date or time.";
                }
            } catch (Exception e) {
                return "Invalid date or time format.";
            }
        }
        return null;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private LocalDateTime determineTransactionDateTime(String date, String time) {
        if (!isEmpty(date) && !isEmpty(time)) {
            return LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
        }
        return LocalDateTime.now();
    }

    private Account getFromAccount(User user) {
        if (user == null || user.getAccounts() == null || user.getAccounts().isEmpty()) {
            return null;
        }
        return accountService.getAccount(user.getAccounts().get(0).getAccountNo());
    }

    private String validateAccountsAndBalance(Account fromAccount, Account toAccount, double amount) {
        if (fromAccount == null) {
            return "Your account is suspended.";
        }
        if (toAccount == null) {
            return "Destination account is suspended or invalid.";
        }
        if (fromAccount.getBalance() < amount) {
            return "Insufficient funds.";
        }
        return null;
    }

    private void processTransaction(Account fromAccount, Account toAccount, double amount,
                                    LocalDateTime dateTime, TransactionStatus status) {
        transactionService.createTransaction(new Transfer(dateTime, status, amount, toAccount, fromAccount));
        if (status == TransactionStatus.COMPLETED) {
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);
            accountService.updateAccount(fromAccount);
            accountService.updateAccount(toAccount);
        }
    }

    private void sendNotifications(Account fromAccount, Account toAccount, double amount,
                                   LocalDateTime dateTime, TransactionStatus status) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Notification fromUserNotification = new Notification();
        fromUserNotification.setUser(fromAccount.getUser());
        fromUserNotification.setDateTime(LocalDateTime.now());

        if (status == TransactionStatus.PENDING) {
            fromUserNotification.setMessage(String.format("$%.2f has been scheduled to be transferred to %s on %s",
                    amount, toAccount.getAccountNo(), formatter.format(dateTime)));
        } else {
            fromUserNotification.setMessage(String.format("$%.2f has been transferred to %s",
                    amount, toAccount.getAccountNo()));
            Notification toUserNotification = new Notification();
            toUserNotification.setMessage(String.format("$%.2f has been transferred to you by %s",
                    amount, fromAccount.getAccountNo()));
            toUserNotification.setUser(toAccount.getUser());
            toUserNotification.setDateTime(LocalDateTime.now());
            notificationService.sendNotification(toUserNotification);
        }
        notificationService.sendNotification(fromUserNotification);
    }

    private void forwardWithError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("message", message);
        req.getRequestDispatcher("customer/home.jsp").forward(req, resp);
    }
}