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
    <title>Login</title>
</head>
<body>
  <h1>Login</h1>

  <form method="POST" action="${pageContext.request.contextPath}/login">
      <table>
          <tr>
              <th>Email</th>
              <td><input type="text" name="emailOrMobile"></td>
          </tr>
          <tr>
              <th>Password</th>
              <td><input type="password" name="password"></td>
          </tr>
          <tr>
              <td><input type="submit" value="Login"></td>
          </tr>
      </table>
  </form>
</body>
</html>
