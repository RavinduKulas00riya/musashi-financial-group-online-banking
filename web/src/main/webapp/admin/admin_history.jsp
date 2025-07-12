<%--
  Created by IntelliJ IDEA.
  User: Ravindu Kulasooriya
  Date: 12/07/2025
  Time: 5:52 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Transaction History</title>
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
        .search-section {
            border: 1px solid #aaa;
            padding: 20px;
            max-width: 800px;
            margin-bottom: 30px;
        }
        .search-section h2 {
            margin-top: 0;
            margin-bottom: 15px;
        }
        .search-section form {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        .search-section input {
            padding: 8px;
            font-size: 14px;
        }
        .search-section select {
            padding: 8px;
            font-size: 14px;
        }
        .search-section button {
            padding: 8px 12px;
            font-size: 14px;
            cursor: pointer;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            max-width: 1000px;
        }
        th, td {
            border: 1px solid #aaa;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f0f0f0;
        }
    </style>
</head>
<body>
<h1>Transaction History</h1>

<div class="nav-buttons">
    <button onclick="location.href='admin_home.jsp'">Dashboard</button>
    <button onclick="location.href='customers.jsp'">Customer Handling</button>
    <button onclick="location.href='admin_history.jsp'">Transaction History</button>
    <button onclick="location.href='${pageContext.request.contextPath}/logout'">Logout</button>
</div>

<!-- Search Area -->
<div class="search-section">
    <h2>Search History</h2>
    <input type="text" name="accountNo" placeholder="Account Number">
    <select name="status">
        <option value="" selected>Transaction Status</option>
        <option value="COMPLETED">Completed</option>
        <option value="PENDING">Pending</option>
    </select>
    <button onclick="loadHistory()">Search</button>
</div>

<!-- Customer Table -->
<table id="historyTable">
    <thead>
    <tr>
        <th>Sender Account No.</th>
        <th>Receiver Account No.</th>
        <th>Amount(USD)</th>
        <th>Date & Time</th>
        <th>Status</th>
    </tr>
    </thead>
    <tbody>

    </tbody>
</table>

<script src="${pageContext.request.contextPath}/admin/admin_history.js"></script>
</body>
</html>
