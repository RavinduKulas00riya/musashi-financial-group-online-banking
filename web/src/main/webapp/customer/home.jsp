<%@ page import="jakarta.ejb.EJB" %>
<%@ page import="lk.jiat.app.core.service.UserService" %>
<%@ page import="lk.jiat.app.core.model.Notification" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %><%--
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
    </style>
</head>
<body>
<h1>Customer Dashboard</h1>

<div class="nav-buttons">
    <button onclick="location.href='home.jsp'">Dashboard</button>
    <button onclick="location.href='history.jsp'">Account Statement</button>
    <button onclick="location.href='schedule.jsp'">Scheduled Operations</button>
</div>

<div class="section">
    <h2>Transfer Funds</h2>
    <form action="${pageContext.request.contextPath}/transfer" method="post">
        <input type="text" name="destination" placeholder="Destination Account No." required>
        <input type="number" name="amount" placeholder="Amount" required>
        <input type="date" name="date" placeholder="Date (Optional)">
        <input type="time" name="time" placeholder="Time (Optional)">
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
        <li><%= n.getMessage() %> — <%= time %></li>
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

