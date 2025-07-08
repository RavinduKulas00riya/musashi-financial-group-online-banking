<%--
  Created by IntelliJ IDEA.
  User: Ravindu Kulasooriya
  Date: 08/07/2025
  Time: 6:21 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Customer Registration</title>
  <style>
    body {
      font-family: sans-serif;
      padding: 50px;
    }
    h1 {
      margin-bottom: 20px;
    }
    form {
      display: flex;
      flex-direction: column;
      max-width: 300px;
    }
    input {
      margin-bottom: 10px;
      padding: 8px;
      font-size: 14px;
    }
    button {
      padding: 8px;
      font-size: 14px;
      cursor: pointer;
    }
  </style>
</head>
<body>
<h1>Customer Registration</h1>
<% String msg = (String) request.getAttribute("message"); %>
<form action="${pageContext.request.contextPath}/register" method="post">
  <% if (msg != null) { %>
  <div><%= msg %></div>
  <% } %>
  <input type="text" name="fullName" placeholder="Full Name" required>
  <input type="email" name="email" placeholder="Email" required>
  <input type="text" name="mobile" placeholder="Mobile" required>
  <input type="password" name="password" placeholder="Password" required>
  <input type="password" name="confirmPassword" placeholder="Confirm Password" required>
  <input type="number" name="amount" placeholder="Initial Amount" required>
  <button type="submit">Register</button>
</form>
</body>
</html>

