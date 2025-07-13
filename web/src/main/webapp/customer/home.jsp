<%@ page import="jakarta.ejb.EJB" %>
<%@ page import="lk.jiat.app.core.service.UserService" %>
<%@ page import="lk.jiat.app.core.model.Notification" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<%@ page import="lk.jiat.app.core.model.User" %>
<%@ page import="lk.jiat.app.core.model.Account" %>
<%--
  Created by IntelliJ IDEA.
  User: Ravindu Kulasooriya
  Date: 07/07/2025
  Time: 4:33 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Customer Dashboard</title>
    <style>
        body {
            font-family: sans-serif;
            padding: 50px;
        }
        h1 {
            margin-bottom: 20px;
        }
        .nav-buttons {
            margin-bottom: 30px;
        }
        .nav-buttons button {
            margin-right: 10px;
            padding: 8px 12px;
            font-size: 14px;
            cursor: pointer;
        }
        .section {
            border: 1px solid #aaa;
            padding: 20px;
            max-width: 400px;
            margin-bottom: 30px;
        }
        .section h2 {
            margin-top: 0;
            margin-bottom: 15px;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        label {
            margin-bottom: 5px;
            font-size: 14px;
            font-weight: bold;
        }
        input {
            margin-bottom: 10px;
            padding: 8px;
            font-size: 14px;
        }
        button[type="submit"] {
            padding: 8px;
            font-size: 14px;
            cursor: pointer;
        }
        .notifications {
            max-width: 400px;
        }
        .notifications h2 {
            margin-bottom: 10px;
        }
        ul {
            padding-left: 20px;
        }
        li {
            margin-bottom: 8px;
        }
        .msg {
            margin-bottom: 10px;
            margin-top: 10px;
            color: red;
        }
    </style>
</head>
<body>
<h1>Customer Dashboard</h1>

<div class="nav-buttons">
    <button onclick="location.href='${pageContext.request.contextPath}/customer/home.jsp'">Dashboard</button>
    <button onclick="location.href='${pageContext.request.contextPath}/customer/history.jsp'">Account Statement</button>
    <button onclick="location.href='${pageContext.request.contextPath}/customer/schedule.jsp'">Scheduled Operations</button>
    <button onclick="location.href='${pageContext.request.contextPath}/logout'">Logout</button>
</div>

<%
    User user = (User) session.getAttribute("user");
    Account account = null;
    if (user != null && user.getAccounts() != null && !user.getAccounts().isEmpty()) {
        account = user.getAccounts().get(0); // assuming only one account per user
    }
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
%>

<div class="section">
    <h2>Profile</h2>
    <div><strong>Full Name:</strong> <%= user != null ? user.getName() : "N/A" %></div>
    <div><strong>Email:</strong> <%= user != null ? user.getEmail() : "N/A" %></div>
    <div><strong>Mobile:</strong> <%= user != null ? user.getMobile() : "N/A" %></div>

    <hr style="margin: 15px 0;">

    <h2>Account</h2>
    <div><strong>Account Number:</strong> <%= account != null ? account.getAccountNo() : "N/A" %></div>
    <div><strong>Balance:</strong> <%= account != null ? "USD " + account.getBalance() : "N/A" %></div>
    <div><strong>Status:</strong> <%= account != null ? account.getStatus() : "N/A" %></div>
    <div><strong>Created:</strong> <%= account != null && account.getCreatedDateTime() != null
            ? account.getCreatedDateTime().format(dtf) : "N/A" %></div>
</div>

<div class="section">
    <h2>Transfer Funds</h2>
    <% String msg = (String) request.getAttribute("message"); %>
    <form action="${pageContext.request.contextPath}/transaction" method="post">
        <% if (msg != null) { %>
        <div class="msg"><%= msg %></div>
        <% } %>
        <label for="destination">Destination Account No.</label>
        <input type="text" id="destination" name="destination" required>
        <label for="amount">Amount</label>
        <input type="number" id="amount" name="amount" required>
        <label for="date">Date (Optional)</label>
        <input type="date" id="date" name="date">
        <label for="time">Time (Optional)</label>
        <input type="time" id="time" name="time">
        <button type="submit">Submit</button>
    </form>
</div>

<div class="notifications">
    <h2>Notifications</h2>
    <%
        List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    %>
    <ul>
        <%
            if (notifications != null && !notifications.isEmpty()) {
                for (Notification n : notifications) {
                    String time = n.getDateTime() != null ? n.getDateTime().format(formatter) : "Unknown time";
        %>
        <li><%= n.getMessage() %> — <strong><%= time %></strong></li>
        <%
            }
        } else {
        %>
        <li>No notifications found.</li>
        <%
            }
        %>
    </ul>
</div>
</body>
</html>