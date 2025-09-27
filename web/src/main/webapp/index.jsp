<%--
  Created by IntelliJ IDEA.
  User: Ravindu Kulasooriya
  Date: 07/07/2025
  Time: 3:48 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <link rel="icon" href="${pageContext.request.contextPath}/images/logowhite.png">
    <style>
        @font-face {
            font-family: "medium";
            src: url("${pageContext.request.contextPath}/fonts/Inter_28pt-Medium.ttf");
        }

        @font-face {
            font-family: "bold";
            src: url("${pageContext.request.contextPath}/fonts/Inter_28pt-SemiBold.ttf");
        }

        @font-face {
            font-family: "regular";
            src: url("${pageContext.request.contextPath}/fonts/Inter_28pt-Regular.ttf");
        }

        .medium {
            font-family: "medium";
        }

        .bold {
            font-family: "bold";
        }

        .regular {
            font-family: "regular";
        }

        body,
        html {
            height: 100%;
            margin: 0;
            overflow: hidden;
        }

        .container-fluid {
            height: 100vh;
        }

        .left-side {
            background-color: #ffffff;
            height: 100%;
            display: flex;
            flex-direction: column;
            padding: 25px;
        }

        .right-side {
            position: relative;
            height: 100%;
            display: flex;
            flex-direction: column;
            color: white;
            text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
        }

        .bg-image {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-size: cover;
            background-position: center;
            opacity: 0;
            transition: opacity 1s ease-in-out;
            z-index: -1;
            filter: brightness(35%);
        }

        .bg-image.active {
            opacity: 1;
        }

        .logo {
            max-width: 180px;
            filter: brightness(170%);
        }

        .login-form {
            max-width: 500px;
            width: 100%;
        }

        .right-side-half {
            text-align: center;
            display: flex;
            flex: 1;
            align-items: center;
            justify-content: center;
        }

        .description {
            max-width: 450px;
            font-size: 20px;
        }

        .footer-text {
            font-size: 0.8rem;
            color: #6c757d;
        }

        .input-container {
            position: relative;
            width: 380px;
            margin-bottom: 10px;
            font-family: medium;
        }

        .buttons {
            width: 380px;
            margin-bottom: 10px;
            display: flex;
            flex-direction: column;
            color: #ffffff;
        }

        .log-btn {
            background-image: linear-gradient(#42A1EC, #0070C9);
            border: 1px solid #0077CC;
            color: #FFFFFF;
            direction: ltr;
            overflow: visible;
        }

        .animated-input {
            width: 100%;
            height: 50px;
            padding: 15px 10px 2px 10px;
            font-size: 17px;
            border: 2px solid #e9e9e9ff;
            border-radius: 10px;
            outline: none;
            background: #f5f5f5;
            transition: border-color 0.3s ease;
        }

        .animated-input:focus {
            border-color: #007bff;
            box-shadow: none;

        }

        .input-label {
            position: absolute;
            top: 50%;
            left: 6px;
            transform: translateY(-50%);
            font-size: 17px;
            color: #6c757d;
            pointer-events: none;
            transition: all 0.3s ease;
        }

        .animated-input:focus + .input-label,
        .animated-input:not(:placeholder-shown) + .input-label {
            top: 0;
            transform: translateY(18%);
            font-size: 12px;
            padding: 0 5px;
        }

        .animated-input:focus + .input-label {
            color: #007bff;
        }

        .error-msg{
            color: red;
            min-height: 24px;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row h-100">
        <!-- Left Side -->
        <div class="col-md-6 left-side">
            <div style="width: 100%; height: 100%; display: flex; flex-direction: column; justify-content: space-around;">
                <div style="width: 100%; height: 22%; display: flex; flex-direction: column; justify-content: space-between;">
                    <img style="width: 42px; height: 42px;" src="${pageContext.request.contextPath}/images/logo.png" alt="">
                    <span class="bold" style="font-size: 46px; padding-left: 38px;">Customer Login</span>
                </div>
                <div style="width: 100%; height: 73%; display: flex; flex-direction: column; justify-content: center; align-items: center;">
                    <span class="regular error-msg" id="error-msg" style="margin-bottom: 10px;"></span>
                    <div class="input-container" onclick="hideError()">
                        <input spellcheck="false" type="text" class="form-control animated-input" name="email"
                               placeholder=" ">
                        <label class="input-label">Email Address</label>
                    </div>
                    <div class="input-container" onclick="hideError()">
                        <input type="password" class="form-control animated-input" name="password" placeholder=" ">
                        <label class="input-label">Password</label>
                    </div>
                    <div class="buttons medium">
                        <button class="log-btn" onclick="login()"
                                style="height: 50px; font-size: 17px; border: none; border-radius: 10px; margin-bottom: 10px;">
                            Login
                        </button>
                        <button style="height: 50px; font-size: 17px; border: none; border-radius: 10px; color: #007bff; margin-bottom: 10px; background-color: rgba(0, 123, 255, 0.05);" onclick="window.location.href='${pageContext.request.contextPath}/register.jsp'">
                            Create a New Account
                        </button>
                    </div>
                </div>
                <div class="regular footer-text text-center" style="width: 100%; height: 5%;">
                    © 2025 Musashi Financial Group, All Rights Reserved
                </div>
            </div>
        </div>
        <!-- Right Side -->
        <div class="col-md-6 right-side">
            <div class="bg-image active" style="background-image: url('${pageContext.request.contextPath}/images/slide1.jpg')"></div>
            <div class="bg-image" style="background-image: url('${pageContext.request.contextPath}/images/slide2.jpg')"></div>
            <div class="bg-image" style="background-image: url('${pageContext.request.contextPath}/images/slide3.jpg')"></div>
            <div class="right-side-half">
                <img src="./images/logowhite.png" alt="Company Logo" class="logo">
            </div>

            <div class="right-side-half">
                <div class="description medium">
                    <p>Welcome to Musashi Financial Group! Seamlessly send money to bank accounts with instant or
                        scheduled transactions.
                        Log in to experience secure, fast, and reliable financial services tailored to your needs.</p>
                </div>

            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/login.js"></script>
</body>

</html>

