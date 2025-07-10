<%--
  Created by IntelliJ IDEA.
  User: Ravindu Kulasooriya
  Date: 10/07/2025
  Time: 10:57 pm
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
        .search-section input,
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
            max-width: 900px;
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
<body onload="searchHistory('PENDING')">
<h1>Scheduled Transactions</h1>

<div class="nav-buttons">
    <button onclick="location.href='home.jsp'">Dashboard</button>
    <button onclick="location.href='history.jsp'">Account Statement</button>
    <button onclick="location.href='schedule.jsp'">Scheduled Operations</button>
</div>

<div class="search-section">
    <h2>Search</h2>
    <h4>Daily interest arrives at EST 00:00 AM</h4>
    <div>
        <input type="text" name="accountNumber" placeholder="Account Number">
        <input type="text" name="name" placeholder="Name">
        <button onclick="searchHistory('PENDING')">Search</button>
    </div>
</div>

<table>
    <thead>
    <tr>
        <th>Account Number</th>
        <th>Name</th>
        <th>Amount (USD)</th>
        <th>Scheduled Date & Time</th>
    </tr>
    </thead>
    <tbody>

    </tbody>
</table>

<script src="${pageContext.request.contextPath}/customer/history.js"></script>
</body>
</html>


