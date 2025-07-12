<%--
  Created by IntelliJ IDEA.
  User: Ravindu Kulasooriya
  Date: 07/07/2025
  Time: 3:44 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Unauthorized</title>
    <style>
        button{
            padding: 10px;
        }
    </style>
</head>
<body>
    <h1>Unauthorized</h1>
    <div style="display: flex; flex-direction: row; gap: 10px;">
        <button onclick="window.location.href='admin_login.jsp'">Back to Admin Login</button>
        <button onclick="window.location.href='index.jsp'">Go to Customer Login</button>
    </div>

</body>
</html>
