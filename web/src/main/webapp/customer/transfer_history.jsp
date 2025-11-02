<%--
  Created by IntelliJ IDEA.
  User: Ravindu Kulasooriya
  Date: 24/10/2025
  Time: 6:00 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Dynamic Web Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/home.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/transfer_history.css"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
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
<body>
<div class="vertical-div" style="width: 100%">
    <div class="horizontal-div" style="margin-bottom: 20px; gap: 20px">
        <div class="vertical-div sort-div">
            <span class="sort-div-span">Account Number</span>
            <input
                    type="text"
                    id="account-num"
                    placeholder="123456789"
                    required
            />
        </div>

        <div class="vertical-div sort-div">
            <span class="sort-div-span">Counterparty</span>
            <input
                    type="text"
                    id="counterparty"
                    placeholder="John Doe"
                    required
            />
        </div>

        <div class="vertical-div sort-div">
            <span class="sort-div-span">Type</span>

            <div class="custom-select" id="type-select">
                <div class="custom-select-trigger" id="type" data-value="0">
                    <span>All</span>
                    <i class="fa fa-caret-down" aria-hidden="true"></i>
                </div>
                <div class="custom-options">
                    <div
                            class="custom-option selected"
                            id="type-default"
                            data-value="0"
                    >
                        All
                    </div>
                    <div class="custom-option" data-value="1">Incoming</div>
                    <div class="custom-option" data-value="2">Outgoing</div>
                </div>
            </div>
        </div>

        <div class="vertical-div sort-div">
            <span class="sort-div-span">Start Date</span>
            <input
                    type="text"
                    class="dateInput"
                    id="startDate"
                    placeholder="DD/MM/YYYY"
                    required
            />
            <div class="error-message" id="startDateError">Invalid date format</div>
        </div>

        <div class="vertical-div sort-div">
            <span class="sort-div-span">End Date</span>
            <input
                    type="text"
                    id="endDate"
                    class="dateInput"
                    placeholder="DD/MM/YYYY"
                    required
            />
            <div class="error-message" id="endDateError">Invalid date format</div>
        </div>

        <div class="sort-btn-div">
            <button class="reset-btn" id="reset">Reset</button>
        </div>
        <div class="sort-btn-div">
            <button class="search-btn" onclick="SocketManager.customerTransferHistorySocket.send('')">Apply</button>
        </div>
    </div>

    <div class="horizontal-div table-top">
        <div>
            <span style="cursor: pointer" onclick="sortByDate()">Date & Time</span>
            <button class="sort-btn" id="sortDate">DESC</button>
        </div>
        <div>
            <span>Account Number</span>
        </div>
        <div>
            <span>Counterparty</span>
        </div>
        <div>
            <span style="cursor: pointer" onclick="sortByAmount()">Amount (USD)</span>
            <button class="sort-btn" id="sortAmount">MIXED</button>
        </div>
        <div>
            <span>Transaction ID</span>
        </div>
    </div>

    <div id="rows">

        <div class="horizontal-div table-row">
            <div class="table-row-div">
                <span>20 Oct 2025, 11:12:43</span>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span><span class="mask">*******</span>147</span>
            </div>
            <div class="table-row-div">
                <span>Walter White</span>
            </div>
            <div class="table-row-div">
                <div class="amount incoming">+ 435.21</div>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span>Xdg3t<span class="mask">**********</span>H7uT8</span>
            </div>
        </div>

        <div class="horizontal-div table-row">
            <div class="table-row-div">
                <span>19 Oct 2025, 15:47:09</span>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span><span class="mask">*******</span>592</span>
            </div>
            <div class="table-row-div">
                <span>Jesse Pinkman</span>
            </div>
            <div class="table-row-div">
                <div class="amount outgoing">− 218.50</div>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span>kKa68<span class="mask">**********</span>P9me4</span>
            </div>
        </div>

        <div class="horizontal-div table-row">
            <div class="table-row-div">
                <span>18 Oct 2025, 09:25:33</span>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span><span class="mask">*******</span>803</span>
            </div>
            <div class="table-row-div">
                <span>John Cena</span>
            </div>
            <div class="table-row-div">
                <div class="amount incoming">+ 1,024.00</div>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span>I0r1s<span class="mask">**********</span>K6xv2</span>
            </div>
        </div>

        <div class="horizontal-div table-row">
            <div class="table-row-div">
                <span>17 Oct 2025, 21:05:58</span>
            </div>
            <div class="table-row-div">
                <i
                        class="fa fa-clone copy-icon"
                        aria-hidden="true"
                        data-tooltip="Copy"
                ></i>
                <span><span class="mask">*******</span>219</span>
            </div>
            <div class="table-row-div">
                <span>Tony Soprano</span>
            </div>
            <div class="table-row-div">
                <div class="amount outgoing">− 97.30</div>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span>4Trrv<span class="mask">**********</span>Bo051</span>
            </div>
        </div>

        <div class="horizontal-div table-row">
            <div class="table-row-div">
                <span>16 Oct 2025, 12:38:17</span>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span><span class="mask">*******</span>774</span>
            </div>
            <div class="table-row-div">
                <span>Gustavo Fring</span>
            </div>
            <div class="table-row-div">
                <div class="amount incoming">+ 2,356.45</div>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span>RTf32<span class="mask">**********</span>M396j</span>
            </div>
        </div>

        <div class="horizontal-div table-row">
            <div class="table-row-div">
                <span>15 Oct 2025, 08:14:26</span>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span><span class="mask">*******</span>390</span>
            </div>
            <div class="table-row-div">
                <span>Charlie Kirk</span>
            </div>
            <div class="table-row-div">
                <div class="amount outgoing">− 450.00</div>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span>45Dha<span class="mask">**********</span>Wi788</span>
            </div>
        </div>

        <div class="horizontal-div table-row">
            <div class="table-row-div">
                <span>14 Oct 2025, 22:59:02</span>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span><span class="mask">*******</span>645</span>
            </div>
            <div class="table-row-div">
                <span>Moe Lester</span>
            </div>
            <div class="table-row-div">
                <div class="amount incoming">+ 785.60</div>
            </div>
            <div class="table-row-div">
                <i class="fa fa-clone" aria-hidden="true"></i>
                <span>b9T43<span class="mask">**********</span>Nfe21</span>
            </div>
        </div>
    </div>



    <div class="center-div table-bottom">
        <button class="prev">
            <i class="fa fa-arrow-left" aria-hidden="true"></i>
        </button>
        <button>3</button>
        <button>4</button>
        <button class="page-active">5</button>
        <button>6</button>
        <button disabled>...</button>
        <button>12</button>
        <button class="next">
            <i class="fa fa-arrow-right" aria-hidden="true"></i>
        </button>
    </div>
</div>

<script id="transfer_history" src="${pageContext.request.contextPath}/customer/transfer_history.js"></script>
</body>
</html>

