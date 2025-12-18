<%--
  Created by IntelliJ IDEA.
  User: Ravindu Kulasooriya
  Date: 13/11/2025
  Time: 12:58 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/home.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/timely_operations.css"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
    />
    <script
            src="https://kit.fontawesome.com/52e3cc1234.js"
            crossorigin="anonymous"
    ></script>
</head>
<body>

<div class="vertical-div" style="width: 100%; gap: 35px">

    <div style="
          width: 100%;
          display: flex;
          justify-content: space-between;
        ">
        <div style="display: flex; gap: 20px">
            <div class="center-div" style="
              background-color: #e3effa;
              padding: 10px;
              border-radius: 0.4rem;
            ">
                <i class="far fa-money-bill-alt" style="font-size: 26px; color: #174a7e"></i>
            </div>
            <div class="vertical-div" style="gap: 10px">
                <span style="font-size: 13px; font-family: medium; color: #555555">Total Value Pending</span>
                <span style="font-size: 25px; font-family: regular" id="totalAmount">USD 12,320.33</span>
            </div>
        </div>
        <div style="display: flex; gap: 20px">
            <div class="center-div" style="
              background-color: #fce8ef;
              padding: 10px;
              border-radius: 0.4rem;
            ">
                <i class="fas fa-bars" style="font-size: 26px; color: #b23a5a"></i>
            </div>
            <div class="vertical-div" style="gap: 10px">
                <span style="font-size: 13px; font-family: medium; color: #555555">Total Pending Transfers</span>
                <span style="font-size: 25px; font-family: regular" id="totalCount">12</span>
            </div>
        </div>
        <div style="display: flex; gap: 20px">
            <div class="center-div" style="
              background-color: #f0eafe;
              padding: 10px;
              border-radius: 0.4rem;
            ">
                <i class="fas fa-calendar-alt" style="font-size: 26px; color: #4b2c82"></i>
            </div>
            <div class="vertical-div" style="gap: 10px">
                <span style="font-size: 13px; font-family: medium; color: #555555">Next Scheduled Date</span>
                <span
                        style="
                font-size: 25px;
                font-family: regular;
                display: inline-flex;
                align-items: center;
                gap: 8px;
              "
                        id="nextScheduled"
                >Dec 12, 2025
              <i
                      class="fas fa-circle"
                      style="font-size: 6px; line-height: 1"
                      aria-hidden="true"
              ></i>
              10:35</span>
            </div>
        </div>
        <div style="display: flex; gap: 20px">
            <div class="center-div" style="
              background-color: #fff3c7;
              padding: 10px;
              border-radius: 0.4rem;
            ">
                <i class="fa fa-clock-o" style="font-size: 26px; color: #c48800"></i>
            </div>
            <div class="vertical-div" style="gap: 10px">
                <span style="font-size: 13px; font-family: medium; color: #555555">Next Daily Interest</span>
                <span style="font-size: 25px; font-family: regular" id="midnight-timer">10:25:12</span>
            </div>
        </div>
    </div>

    <div class="vertical-div" style="width: 100%">
        <div class="horizontal-div" style="margin-bottom: 20px; gap: 20px">
            <button class="table-top-btn" id="openFilters">
                <i class="fas fa-filter" style="margin-right: 5px" aria-hidden="true"></i>Filter
            </button>

            <button class="table-top-btn" id="openSort">
                <i class="fas fa-sort" style="margin-right: 5px" aria-hidden="true"></i>Sort
            </button>
            <div class="refresh-btn-div">
                <button class="refresh-btn" id="refresh-btn">
                    <i class="fas fa-refresh" style="margin-right: 5px" aria-hidden="true"></i>
                    Refresh
                </button>
                <div class="refresh-msg" id="refresh-msg">New Record(s) Available</div>
            </div>
        </div>

        <div class="horizontal-div table-top">
            <div>
                <span>Scheduled Time</span>
            </div>
            <div>
                <span>Created Time</span>
            </div>
            <div>
                <span>Account Number</span>
            </div>
            <div>
                <span>Counterparty</span>
            </div>
            <div>
                <span>Amount (USD)</span>
            </div>
            <div>
                <span>Status</span>
            </div>
        </div>

        <div id="rows">

            <div class="horizontal-div table-row">
                <div class="table-row-div">
                    <div class="center-div" style="gap: 6px">
                        <span>24 Dec 2025</span>
                        <i class="fas fa-circle" style="font-size: 4px" aria-hidden="true"></i>
                        <span>23:45</span>
                    </div>
                </div>
                <div class="table-row-div">
                    <div class="center-div" style="gap: 6px">
                        <span>26 Nov 2025</span>
                        <i class="fas fa-circle" style="font-size: 4px" aria-hidden="true"></i>
                        <span>10:45:12</span>
                    </div>
                </div>
                <div class="table-row-div">
                    <i class="fa fa-clone copy-icon" aria-hidden="true" data-tooltip="362517537"></i>
                    <span><span class="mask">*******</span>537</span>
                </div>
                <div class="table-row-div">
                    <span>Tony Soprano</span>
                </div>
                <div class="table-row-div">
                    <div class="amount">97.30</div>
                </div>
                <div class="table-row-div">
                    <button class="status-btn pending">
                        <i class="fas fa-play" style="margin-right: 6px" aria-hidden="true"></i>Pending
                    </button>
                </div>
            </div>

        </div>

        <div class="center-div table-bottom">
            <button class="prev">
                <i class="fa fa-arrow-left" aria-hidden="true"></i>
            </button>
            <button class="page-button">3</button>
            <button class="page-button">4</button>
            <button class="page-button page-active">5</button>
            <button class="page-button">6</button>
            <button style="cursor: default;" disabled>...</button>
            <button class="page-button">12</button>
            <button class="next">
                <i class="fa fa-arrow-right" aria-hidden="true"></i>
            </button>
        </div>
    </div>

</div>

<div class="center-div to_overlay" id="TO_overlay">
    <!-- filters window -->
    <div class="filter-div" id="filterDiv">
        <span class="filter-div-title">Filter</span>

        <div class="filter-div-content">
            <div class="vertical-div center-div filter" style="gap: 12px">
                <span>Scheduled Date Range</span>

                <div
                        class="horizontal-div"
                        style="width: 100%; justify-content: space-between"
                >
                    <input
                            type="text"
                            id="scheduledStart"
                            placeholder="DD/MM/YYYY"
                            required
                    />
                    <i class="fa fa-minus" aria-hidden="true"></i>
                    <input
                            type="text"
                            id="scheduledEnd"
                            placeholder="DD/MM/YYYY"
                            required
                    />
                </div>
            </div>

            <div class="vertical-div center-div filter" style="gap: 12px">
                <span>Created Date Range</span>

                <div
                        class="horizontal-div"
                        style="width: 100%; justify-content: space-between"
                >
                    <input
                            type="text"
                            id="createdStart"
                            placeholder="DD/MM/YYYY"
                            required
                    />
                    <i class="fa fa-minus" aria-hidden="true"></i>
                    <input
                            type="text"
                            id="createdEnd"
                            placeholder="DD/MM/YYYY"
                            required
                    />
                </div>
            </div>

            <div class="horizontal-div" style="gap: 40px">
                <div class="vertical-div filter" style="gap: 12px">
                    <span>Account Number</span>
                    <input
                            type="text"
                            id="accountNum"
                            placeholder="01234567"
                            required
                    />
                </div>
                <div class="vertical-div filter" style="gap: 12px">
                    <span>Counterparty</span>
                    <input
                            type="text"
                            id="counterparty"
                            placeholder="John Doe"
                            required
                    />
                </div>
            </div>

            <div class="horizontal-div filter-status-btn-div">
                <button class="active-filter-status" id="status-all">All</button>
                <button class="inactive-filter-status" id="status-pending">Pending</button>
                <button class="inactive-filter-status" id="status-paused">Paused</button>
            </div>

            <div class="horizontal-div" style="gap: 40px">
                <button class="reset-btn" id="resetBtn">Clear Filters</button>
                <button class="filter-btn" id="applyBtn">Apply Filters</button>
            </div>
        </div>
    </div>

    <!-- sort window -->
    <div class="sort-div" id="sortDiv">
        <span class="filter-div-title">Sort</span>

        <div class="sort-div-content">
            <div class="center-div sort-sub-div">
                <span>Scheduled Time</span>
                <div class="center-div" style="gap: 20px">
                    <button class="active-sort" id="scheduledDesc">DESC</button>
                    <button class="inactive-sort" id="scheduledAsc">ASC</button>
                </div>
            </div>
            <div class="center-div sort-sub-div">
                <span>Created Time</span>
                <div class="center-div" style="gap: 20px">
                    <button class="inactive-sort" id="createdDesc">DESC</button>
                    <button class="inactive-sort" id="createdAsc">ASC</button>
                </div>
            </div>
            <div class="center-div sort-sub-div">
                <span>Amount</span>
                <div class="center-div" style="gap: 20px">
                    <button class="inactive-sort" id="amountDesc">DESC</button>
                    <button class="inactive-sort" id="amountAsc">ASC</button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
