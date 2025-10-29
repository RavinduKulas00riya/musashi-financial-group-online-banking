<%@ page import="jakarta.ejb.EJB" %>
<%@ page import="lk.jiat.app.core.service.UserService" %>
<%@ page import="lk.jiat.app.core.model.Notification" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<%@ page import="lk.jiat.app.core.model.User" %>
<%@ page import="lk.jiat.app.core.model.Account" %>
<%@ page import="lk.jiat.app.core.model.AccountStatus" %>
<%--
  Created by IntelliJ IDEA.
  User: Ravindu Kulasooriya
  Date: 07/07/2025
  Time: 4:33 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Dynamic Web Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/home.css"/>
    <link
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css"
    />
    <script
            src="https://kit.fontawesome.com/52e3cc1234.js"
            crossorigin="anonymous"
    ></script>
</head>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");
%>
<body onload="loadPage('transfer_history')">
<div id="divA" data-context-path="${pageContext.request.contextPath}">
    <div style="height: 90px">
        <img
                src="${pageContext.request.contextPath}/images/logo.png"
                style="width: 60px; height: 60px"
                alt=""
        />
    </div>
    <div style="padding-left: 10px">
        <div class="navigator-titles-div">
            <span class="navigator-titles">PAGES</span>
        </div>
        <button class="navigator active-navigator"
                onclick="loadPage('dashboard')"
                id="dashboard">
            <img
                    src="${pageContext.request.contextPath}/images/dashboard2.png"
                    width="16"
                    height="16"
                    alt=""
            />
            <span>Dashboard</span>
        </button>
        <button class="navigator inactive-navigator"
                onclick="loadPage('transfer_history')"
                id="transfer_history">
            <img
                    src="${pageContext.request.contextPath}/images/file.png"
                    width="16"
                    height="16"
                    alt=""
            />
            <span>Transfer History</span>
        </button>
        <button class="navigator inactive-navigator"
                onclick="loadPage('scheduled_operations')"
                id="scheduled_operations">
            <img
                    src="${pageContext.request.contextPath}/images/stopwatch.png"
                    width="16"
                    height="16"
                    alt=""
            />
            <span>Timely Operations</span>
        </button>
    </div>
    <div style="padding-left: 12px">
        <div class="navigator-titles-div">
            <span class="navigator-titles">SETTINGS</span>
        </div>
        <button class="navigator inactive-navigator">
            <img
                    src="${pageContext.request.contextPath}/images/settings.png"
                    width="16"
                    height="16"
                    alt=""
            />
            <span>Preferences</span>
        </button>
    </div>
    <div style="padding-left: 12px">
        <div class="navigator-titles-div">
            <span class="navigator-titles">NOTIFICATIONS</span>
        </div>
        <button class="navigator inactive-navigator inbox-div" id="notificationsBtn">
            <img
                    src="${pageContext.request.contextPath}/images/notification.png"
                    width="16"
                    height="16"
                    alt=""
                    id="notification-icon"
            />
            <span>Inbox</span>
            <div
                    class="center-div"
                    style="
              border: 1px solid #0d0d0d46;
              border-radius: 0.4rem;
              width: 45px;
              height: 23px;
              display: none;
            "
                    id="notification-new-div"
            >
                <p style="font-size: 12px; color: #2b2b2bda">New!</p>
            </div>
        </button>
    </div>
</div>

<div id="divB">
    <div
            style="
          width: 100%;
          display: flex;
          justify-content: space-between;
          align-items: center;
        "
    >
        <h1 style="font-family: medium" id="page-title">Dashboard</h1>
        <div class="horizontal-div" style="gap: 35px">
            <button class="horizontal-div top-btn help-btn">
                <i class="fa fa-question-circle-o" style="font-size: 15px; margin-top: 1.6px"></i>
                <span style="font-size: 12.7px">help?</span>
            </button>
            <button class="horizontal-div top-btn logout-btn"
                    onclick="location.href='${pageContext.request.contextPath}/logout'">
                <i class="fa fa-sign-out" style="font-size: 15px; margin-top: 1.6px"></i>
                <span style="font-size: 12.7px">Log out</span>
            </button>
        </div>
    </div>

    <div id="panel" style="display: flex; width: 100%">

    </div>

</div>

<div id="divC">
    <%--      <span style="font-family: medium; font-size: 12px; color: #727272"--%>
    <%--      >New</span--%>
    <%--      >--%>
    <%--    <div class="notification">--%>
    <%--        <span>USD 200.0 has been transferred to you by 8436543</span>--%>
    <%--        <span class="notification-time">Today</span>--%>
    <%--    </div>--%>
    <%--    <div class="notification">--%>
    <%--        <span>$0.3 of daily interest has been added to your balance.</span>--%>
    <%--        <span class="notification-time">Yesterday</span>--%>
    <%--    </div>--%>
    <%--    <span style="font-family: medium; font-size: 12px; color: #727272"--%>
    <%--    >Old</span--%>
    <%--    >--%>
    <%--    <div class="notification">--%>
    <%--        <span>USD 400.0 has been transferred to you by 8436543</span>--%>
    <%--        <span class="notification-time">This Week</span>--%>
    <%--    </div>--%>
</div>
<div class="overlay" id="overlay"></div>

<script id="home" src="${pageContext.request.contextPath}/home.js">
</script>
</body>
</html>