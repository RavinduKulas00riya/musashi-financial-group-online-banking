<%--
  Created by IntelliJ IDEA.
  User: Ravindu Kulasooriya
  Date: 07/07/2025
  Time: 5:52 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>

<html>
<head>
  <title>Admin Login</title>
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
<h1>Admin Login</h1>
<% String msg = (String) request.getAttribute("message"); %>
<form action="${pageContext.request.contextPath}/admin_login" method="post">
  <% if (msg != null) { %>
  <div><%= msg %></div>
  <% } %>
  <input type="text" placeholder="Email" name="email" required>
  <input type="password" placeholder="Password" name="password" required>
  <button type="submit">Login</button>
</form>
</body>
</html>

