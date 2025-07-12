<%@ page import="lk.jiat.app.core.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Dashboard</title>
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
            max-width: 500px;
            margin-bottom: 30px;
        }
        .section h2 {
            margin-top: 0;
            margin-bottom: 15px;
        }
        .section div {
            margin-bottom: 8px;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            max-width: 600px;
        }
        th, td {
            border: 1px solid #aaa;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f0f0f0;
        }
        .section-title {
            font-size: 18px;
            margin: 20px 0 10px 0;
        }
    </style>
</head>
<body onload="loadDashboard()">
<h1>Admin Dashboard</h1>

<div class="nav-buttons">
    <button onclick="location.href='admin_home.jsp'">Dashboard</button>
    <button onclick="location.href='customers.jsp'">Customer Handling</button>
    <button onclick="location.href='admin_history.jsp'">Transaction History</button>
    <button onclick="location.href='${pageContext.request.contextPath}/logout'">Logout</button>
</div>

<%
    User user = (User) request.getSession().getAttribute("admin");
%>

<!-- Admin Details Section -->
<div class="section">
    <h2>Admin Details</h2>
    <div><strong>Full Name:</strong> <%= user.getName()%></div>
    <div><strong>Email:</strong> <%= user.getEmail()%></div>
    <div><strong>Mobile:</strong> <%= user.getMobile()%></div>
</div>

<div class="section" id="statsSection">
    <h2>Statistics</h2>
    <div><strong>Total Customers:</strong> <span id="totalCustomers">-</span></div>
    <div><strong>Active Customers:</strong> <span id="activeCustomers">-</span></div>
    <div><strong>Current Total Balance:</strong> USD <span id="totalBalance">-</span></div>
</div>

<!-- Daily Balance Records Table -->
<div class="section-title">Daily Balance Records</div>
<table id="balanceTable">
    <thead>
    <tr>
        <th>Date</th>
        <th>Balance (USD)</th>
        <th>Change (%)</th>
    </tr>
    </thead>
    <tbody>
    <!-- Filled by JS -->
    </tbody>
</table>

<script src="${pageContext.request.contextPath}/admin/admin_home.js"></script>
</body>
</html>
