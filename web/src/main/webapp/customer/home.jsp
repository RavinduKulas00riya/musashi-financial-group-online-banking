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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common.css"/>
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
    User user = (User) session.getAttribute("user");
    Account account = null;
    if (user != null && user.getAccounts() != null && !user.getAccounts().isEmpty()) {
        account = user.getAccounts().get(0); // assuming only one account per user
    }
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<body>
<div id="divA">
    <div style="height: 90px">
        <img
                src="${pageContext.request.contextPath}/images/logo.png"
                style="width: 60px; height: 60px"
                alt=""
        />
    </div>
    <div style="padding-left: 10px">
        <div class="header-titles-div">
            <span class="header-titles">PAGES</span>
        </div>
        <button class="header active-header"
                onclick="location.href='${pageContext.request.contextPath}/customer/home.jsp'">
            <img
                    src="${pageContext.request.contextPath}/images/dashboard2.png"
                    width="16"
                    height="16"
                    alt=""
            />
            <span>Dashboard</span>
        </button>
        <button class="header inactive-header"
                onclick="location.href='${pageContext.request.contextPath}/customer/history.jsp'">
            <img
                    src="${pageContext.request.contextPath}/images/file.png"
                    width="16"
                    height="16"
                    alt=""
            />
            <span>Transfer History</span>
        </button>
        <button class="header inactive-header"
                onclick="location.href='${pageContext.request.contextPath}/customer/schedule.jsp'">
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
        <div class="header-titles-div">
            <span class="header-titles">SETTINGS</span>
        </div>
        <button class="header inactive-header">
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
        <div class="header-titles-div">
            <span class="header-titles">NOTIFICATIONS</span>
        </div>
        <button class="header inbox-div" id="notificationsBtn">
            <img
                    src="${pageContext.request.contextPath}/images/notification2.png"
                    width="16"
                    height="16"
                    alt=""
            />
            <span>Inbox</span>
            <div
                    class="center-div"
                    style="
              border: 1px solid #0d0d0d46;
              border-radius: 0.4rem;
              width: 45px;
              height: 23px;
            "
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
        <h1 style="font-family: medium">Dashboard</h1>
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

    <div style="display: flex; gap: 35px">
        <!-- <div style="border: 1px solid black; width: 40px; height: 1px"></div> -->
        <!-- <span style="font-size: 20px; font-family: regular"
          >Welcome John Doe.</span
        > -->

        <div class="dashboard-div">
            <div
                    style="
              display: flex;
              flex: 1;
              align-items: center;
              font-size: 35px;
              font-family: bold;
              margin-left: 10px;
              color: white;
              gap: 40px;
            "
            >
                <span>Hello, <%= user != null ? user.getName() : "N/A" %></span>
            </div>
            <div style="flex: 1; position: relative">
                <img
                        src="${pageContext.request.contextPath}/images/dollar.png"
                        width="76"
                        height="76"
                        alt=""
                        class="dashboard-icon"
                        style="
                top: 33%;
                right: 25%;
                animation-duration: 7s;
                animation-delay: 1s;
              "
                />
                <img
                        src="${pageContext.request.contextPath}/images/dong.png"
                        width="70"
                        height="70"
                        alt=""
                        class="dashboard-icon"
                        style="
                top: -5%;
                right: 5%;
                animation-duration: 6s;
                animation-delay: 1s;
              "
                />
                <img
                        src="${pageContext.request.contextPath}/images/euro.png"
                        width="54"
                        height="54"
                        alt=""
                        class="dashboard-icon"
                        style="
                top: 49%;
                right: 4%;
                animation-duration: 7s;
                animation-delay: 2s;
              "
                />
                <img
                        src="${pageContext.request.contextPath}/images/franc.png"
                        width="60"
                        height="60"
                        alt=""
                        class="dashboard-icon"
                        style="bottom: -9%; right: 18%; animation-duration: 6.5s"
                />
                <img
                        src="${pageContext.request.contextPath}/images/yen.png"
                        width="56"
                        height="56"
                        alt=""
                        class="dashboard-icon"
                        style="
                top: 2%;
                right: 40%;
                animation-duration: 6s;
                animation-delay: 1s;
              "
                />
                <img
                        src="${pageContext.request.contextPath}/images/peso.png"
                        width="50"
                        height="50"
                        alt=""
                        class="dashboard-icon"
                        style="
                bottom: 6%;
                left: 40%;
                animation-duration: 5s;
                animation-delay: 2s;
              "
                />
                <img
                        src="${pageContext.request.contextPath}/images/yuan.png"
                        width="40"
                        height="40"
                        alt=""
                        class="dashboard-icon"
                        style="
                top: 33%;
                left: 30%;
                animation-duration: 5s;
                animation-delay: 3s;
              "
                />
                <img
                        src="${pageContext.request.contextPath}/images/lira.png"
                        width="35"
                        height="35"
                        alt=""
                        class="dashboard-icon"
                        style="bottom: 1%; left: 20%; animation-duration: 7s"
                />
                <img
                        src="${pageContext.request.contextPath}/images/naira.png"
                        width="40"
                        height="40"
                        alt=""
                        class="dashboard-icon"
                        style="
                top: -1%;
                left: 25%;
                animation-duration: 7s;
                animation-delay: 1s;
              "
                />
                <img
                        src="${pageContext.request.contextPath}/images/pound.png"
                        width="35"
                        height="35"
                        alt=""
                        class="dashboard-icon"
                        style="top: 32%; left: 10%; animation-duration: 6s"
                />
                <img
                        src="${pageContext.request.contextPath}/images/ruble.png"
                        width="32"
                        height="32"
                        alt=""
                        class="dashboard-icon"
                        style="
                bottom: 20%;
                left: 6%;
                animation-duration: 4s;
                animation-delay: 3s;
              "
                />
                <img
                        src="${pageContext.request.contextPath}/images/rupee.png"
                        width="40"
                        height="40"
                        alt=""
                        class="dashboard-icon"
                        style="top: 5%; left: 5%; animation-delay: 7.5s"
                />
            </div>
        </div>

        <div class="vertical-div" style="flex: 1; gap: 35px">
            <div class="horizontal-div" style="gap: 35px">
                <div class="info-box">
                    <span class="info-box-title">Account Number</span>
                    <span class="info-box-details"><%= account != null ? account.getAccountNo() : "N/A" %></span>
                </div>
                <div class="info-box">
                    <span class="info-box-title">Balance</span>
                    <span class="info-box-details" id="balance">N/A</span>
                </div>
            </div>
            <div class="horizontal-div" style="gap: 35px">
                <div class="info-box">
                    <span class="info-box-title">Account Opened</span>
                    <span class="info-box-details"><%= account != null && account.getCreatedDateTime() != null
                            ? account.getCreatedDateTime().format(dtf) : "N/A" %></span>
                </div>
                <div class="info-box">
                    <span class="info-box-title">Status</span>
                    <div style="display: flex; flex-direction: row" id="status-div">
                        <%--                        <% if(account.getStatus().equals(AccountStatus.ACTIVE)){--%>
                        <%--                            %>--%>
                        <%--                        <div class="status-box active-status">--%>
                        <%--                            <span style="font-family: medium">Active</span>--%>
                        <%--                            <i style="height: 14px" class="bi bi-check-lg"></i>--%>
                        <%--                        </div>--%>
                        <%--                        <% } else { %>--%>
                        <%--                        <div class="status-box suspended-status">--%>
                        <%--                            <span style="font-family: medium">Suspended</span>--%>
                        <%--                            <i style="height: 14px" class="bi bi-x-lg"></i>--%>
                        <%--                        </div>--%>
                        <%--                        <% } %>--%>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="horizontal-div" style="gap: 35px">
        <div class="vertical-div submit-div">
            <span class="info-box-title">Submit Transaction</span>
            <div
                    style="
              padding: 20px;
              display: flex;
              flex-direction: column;
              gap: 20px;
            "
            >
                <div class="horizontal-div" style="gap: 20px">
                    <div class="input-container" style="width: 330px">
                        <input
                                spellcheck="false"
                                type="text"
                                class="form-control animated-input"
                                name="accountNo"
                                placeholder=" "
                        />
                        <label class="input-label">Destination Account Number</label>
                    </div>
                    <div class="input-container" style="width: 200px">
                        <input
                                spellcheck="false"
                                type="text"
                                class="form-control animated-input"
                                name="amount"
                                placeholder=" "
                        />
                        <label class="input-label">Amount (USD)</label>
                    </div>
                </div>
                <div class="horizontal-div" style="gap: 20px">
                    <div
                            class="horizontal-div"
                            style="gap: 10px; align-items: center"
                    >
                        <span style="font-family: medium; font-size: 12px; color: #6c757d">Transfer Later?</span>
                        <input type="checkbox" id="transferLaterCheckbox"/>
                    </div>

                    <div class="input-container" style="width: 160px">
                        <input
                                id="dateInput"
                                spellcheck="false"
                                type="text"
                                class="form-control animated-input"
                                name="date"
                                placeholder=" "
                                maxlength="10"
                                inputmode="numeric"
                                autocomplete="off"
                                disabled
                                style="cursor: not-allowed"
                        />
                        <label class="input-label">Date (D/M/Y)</label>
                    </div>

                    <div class="input-container" style="width: 150px">
                        <input
                                id="timeInput"
                                spellcheck="false"
                                type="text"
                                class="form-control animated-input"
                                name="time"
                                placeholder=" "
                                maxlength="5"
                                inputmode="numeric"
                                autocomplete="off"
                                disabled
                                style="cursor: not-allowed"
                        />
                        <label class="input-label">Time (H:M)</label>
                    </div>
                </div>
            </div>
            <div style="width: 100px; padding-left: 20px">
                <button
                        class="submit-btn"
                        style="
                height: 45px;
                min-width: 120px;
                font-size: 17px;
                border-radius: 10px;
              "
                >
                    Submit
                </button>
            </div>
        </div>

        <div class="vertical-div" style="width: 100%; gap: 35px;">
            <div id="latest-sent-div" class="vertical-div latest-div">
                <span class="info-box-title">Latest Sent Transfer</span>
                <div class="vertical-div" style="gap: 20px">
                    <div
                            class="horizontal-div"
                            style="
                  margin-left: 12px;
                  margin-right: 12px;
                  gap: 28px;
                  font-family: medium;
                "
                    >
                        <div>
                  <span style="margin-left: 4px; font-size: 15px; color: #6c757d"
                  >Account Number</span
                  >
                            <span style="margin-left: 4px; font-size: 18px; color: #000000" id="sentNumber">N/A</span>
                        </div>
                        <div>
                  <span style="margin-left: 4px; font-size: 15px; color: #6c757d"
                  >Receiver's Name</span
                  >
                            <span style="margin-left: 4px; font-size: 18px; color: #000000" id="sentName"
                            >N/A</span
                            >
                        </div>
                    </div>
                    <div
                            class="horizontal-div"
                            style="
                  margin-left: 12px;
                  margin-right: 12px;
                  gap: 28px;
                  font-family: medium;
                "
                    >
                        <div>
                            <span style="margin-left: 4px; font-size: 15px; color: #6c757d">Amount</span>
                            <span style="margin-left: 4px; font-size: 18px; color: #000000" id="sentAmount"
                            >N/A</span
                            >
                        </div>
                        <div>
                  <span style="margin-left: 4px; font-size: 15px; color: #6c757d"
                  >Date</span
                  >
                            <span style="margin-left: 4px; font-size: 18px; color: #000000" id="sentDate"
                            >N/A</span
                            >
                        </div>
                        <div>
                  <span style="margin-left: 4px; font-size: 15px; color: #6c757d"
                  >Time</span
                  >
                            <span style="margin-left: 4px; font-size: 18px; color: #000000" id="sentTime"
                            >N/A</span
                            >
                        </div>
                    </div>
                </div>
            </div>

            <div id="latest-received-div" class="vertical-div latest-div">
                <span class="info-box-title">Latest Received Transfer</span>
                <div class="vertical-div" style="gap: 20px">
                    <div
                            class="horizontal-div"
                            style="
                  margin-left: 12px;
                  margin-right: 12px;
                  gap: 28px;
                  font-family: medium;
                "
                    >
                        <div>
                  <span style="margin-left: 4px; font-size: 15px; color: #6c757d"
                  >Account Number</span
                  >
                            <span style="margin-left: 4px; font-size: 18px; color: #000000"
                                  id="receivedNumber">N/A</span>
                        </div>
                        <div>
                  <span style="margin-left: 4px; font-size: 15px; color: #6c757d"
                  >Sender's Name</span
                  >
                            <span style="margin-left: 4px; font-size: 18px; color: #000000" id="receivedName"
                            >N/A</span
                            >
                        </div>
                    </div>
                    <div
                            class="horizontal-div"
                            style="
                  margin-left: 12px;
                  margin-right: 12px;
                  gap: 28px;
                  font-family: medium;
                "
                    >
                        <div>
                            <span style="margin-left: 4px; font-size: 15px; color: #6c757d">Amount</span>
                            <span style="margin-left: 4px; font-size: 18px; color: #000000" id="receivedAmount"
                            >N/A</span
                            >
                        </div>
                        <div>
                  <span style="margin-left: 4px; font-size: 15px; color: #6c757d"
                  >Date</span
                  >
                            <span style="margin-left: 4px; font-size: 18px; color: #000000" id="receivedDate"
                            >N/A</span
                            >
                        </div>
                        <div>
                  <span style="margin-left: 4px; font-size: 15px; color: #6c757d"
                  >Time</span
                  >
                            <span style="margin-left: 4px; font-size: 18px; color: #000000" id="receivedTime"
                            >N/A</span
                            >
                        </div>
                    </div>
                </div>
            </div>
        </div>
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

<script src="${pageContext.request.contextPath}/dashboard.js">
</script>
</body>
</html>