<%--
  Created by IntelliJ IDEA.
  User: Ravindu Kulasooriya
  Date: 07/07/2025
  Time: 3:48 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Customer Login</title>
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
            margin-bottom: 10px;
        }
        .redirect-btn{
            background: none;
        }
        div{
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<h1>Customer Login</h1>
<% String msg = (String) request.getAttribute("message"); %>
<form action="${pageContext.request.contextPath}/login" method="post">
    <% if (msg != null) { %>
    <div><%= msg %></div>
    <% } %>
    <input type="text" placeholder="Email" name="email" required>
    <input type="password" placeholder="Password" name="password" required>
    <button type="submit">Login</button>
    <button class="redirect-btn" onclick="location.href='register.jsp'">New User? Create New Account</button>
</form>
</body>
</html>

