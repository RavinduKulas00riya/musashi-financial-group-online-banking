(() => {
    if (window.TimelyOperationsPage) {
        return window.TimelyOperationsPage;
    }

    let socket = null;

    let accountNum = "";
    let counterparty = "";
    let scheduledStart = "";
    let scheduledEnd = "";
    let createdStart = "";
    let createdEnd = "";
    let status = "All";
    let sortBy = "scheduledAsc";

    let active_page = 1;

    let midnightIntervalId = null;

    function init() {
        console.log("TimelyOperationsPage initialized");

        setupOverlayAndSortFilterButtons();
        connectWebSocket();

        setTimeout(() => {
            updateMidnightCountdown();
            midnightIntervalId = setInterval(updateMidnightCountdown, 1000);
        }, 1000 - (Date.now() % 1000));
    }

    function renderPagination(currentPage, totalPages) {

        const container = document.querySelector(".table-bottom");
        container.innerHTML = "";

        if(totalPages > 0) {

            if (currentPage !== 1) {
                const prev = document.createElement("button");
                prev.classList.add("prev");
                prev.innerHTML = `<i class="fa fa-arrow-left"></i>`;
                prev.onclick = () => loadTHPage(currentPage - 1);
                container.appendChild(prev);
            }

            if (totalPages > 1) {
                for (
                    let i = Math.max(1, currentPage - 2);
                    i <= Math.min(totalPages, currentPage + 2);
                    i++
                ) {
                    const btn = document.createElement("button");
                    btn.classList.add("page-button");
                    if (i === currentPage) btn.classList.add("page-active");
                    btn.textContent = i;
                    btn.onclick = () => loadTHPage(i);
                    container.appendChild(btn);
                }
            }


            if (currentPage !== totalPages) {
                const next = document.createElement("button");
                next.classList.add("next");
                next.innerHTML = `<i class="fa fa-arrow-right"></i>`;
                next.onclick = () => loadTHPage(currentPage + 1);
                container.appendChild(next);
            }
        }
    }

    function loadTHPage(page) {
        active_page = page || 1;
        setFilters();
        if (socket && socket.readyState === WebSocket.OPEN)
            socket.send(filters());
    }

    function updateMidnightCountdown() {
        const now = new Date();

        // Next midnight
        const midnight = new Date();
        midnight.setHours(24, 0, 0, 0);

        const diffMs = midnight - now;

        if (diffMs <= 0) {
            document.getElementById("midnight-timer").textContent = "00:00:00";
            return;
        }

        const totalSeconds = Math.floor(diffMs / 1000);
        const hours = String(Math.floor(totalSeconds / 3600)).padStart(2, "0");
        const minutes = String(Math.floor((totalSeconds % 3600) / 60)).padStart(2, "0");
        const seconds = String(totalSeconds % 60).padStart(2, "0");

        document.getElementById("midnight-timer").textContent =
            `${hours}:${minutes}:${seconds}`;
    }

    function loadRows(rows){
        const parent = document.getElementById("rows");
        parent.innerHTML = "";
        rows.forEach(row => {
            parent.appendChild(createTableRow(row));
        })
        setupCopyButtons();
    }

    function createTableRow(rowData) {
        const row = document.createElement("div");
        row.className = "horizontal-div table-row";

        // ---------- helper: date • time cell ----------
        function createDateTimeCell(date, time) {
            const cell = document.createElement("div");
            cell.className = "table-row-div";

            const center = document.createElement("div");
            center.className = "center-div";
            center.style.gap = "6px";

            const dateSpan = document.createElement("span");
            dateSpan.textContent = date;

            const dot = document.createElement("i");
            dot.className = "fas fa-circle";
            dot.style.fontSize = "4px";
            dot.setAttribute("aria-hidden", "true");

            const timeSpan = document.createElement("span");
            timeSpan.textContent = time;

            center.append(dateSpan, dot, timeSpan);
            cell.appendChild(center);

            return cell;
        }

        // ---------- column 1: scheduled date & time ----------
        row.appendChild(
            createDateTimeCell(
                rowData.scheduledDate,
                rowData.scheduledTime
            )
        );

        // ---------- column 2: created date & time ----------
        row.appendChild(
            createDateTimeCell(
                rowData.createdDate,
                rowData.createdTime
            )
        );

        // ---------- column 3: masked account + copy icon ----------
        const accountCell = document.createElement("div");
        accountCell.className = "table-row-div";

        const copyIcon = document.createElement("i");
        copyIcon.className = "fa fa-clone copy-icon";
        copyIcon.setAttribute("aria-hidden", "true");
        copyIcon.dataset.tooltip = rowData.accountNum;

        const accountSpan = document.createElement("span");
        accountSpan.innerHTML = `<span class="mask">*******</span>${rowData.accountNum.slice(-3)}`;

        accountCell.append(copyIcon, accountSpan);
        row.appendChild(accountCell);

        // ---------- column 4: counterparty ----------
        const partyCell = document.createElement("div");
        partyCell.className = "table-row-div";
        partyCell.innerHTML = `<span>${rowData.counterparty}</span>`;
        row.appendChild(partyCell);

        // ---------- column 5: amount ----------
        const amountCell = document.createElement("div");
        amountCell.className = "table-row-div";

        const amountDiv = document.createElement("div");
        amountDiv.className = "amount";
        amountDiv.textContent = rowData.amount;

        amountCell.appendChild(amountDiv);
        row.appendChild(amountCell);

        // ---------- column 6: status ----------
        const statusCell = document.createElement("div");
        statusCell.className = "table-row-div";

        const statusBtn = document.createElement("button");
        statusBtn.className = `status-btn ${rowData.status.toLowerCase()}`;
        statusBtn.addEventListener("click", async() => {
            await fetch(`${window.CONTEXT_PATH}/changeScheduledTransactionStatus?id=`+rowData.id);
            // const message = await response.text();
            // alert(message);
            // if (message === "success") {
            //     reset();
            // } else if (message === "redirect") {
            //     await fetch(`${window.CONTEXT_PATH}/logout`);
            //     window.location.href = `${window.CONTEXT_PATH}/index.jsp`;
            // } else {
            //     showError(message);
            // }
        })

        const statusIcon = document.createElement("i");
        if(rowData.status.toLowerCase() === "pending") {
            statusIcon.className = "fas fa-play";
        }else{
            statusIcon.className = "fas fa-pause";
        }
        statusIcon.style.marginRight = "6px";
        statusIcon.setAttribute("aria-hidden", "true");

        statusBtn.append(statusIcon, document.createTextNode(rowData.status.charAt(0).toUpperCase() + rowData.status.slice(1)));
        statusCell.appendChild(statusBtn);
        row.appendChild(statusCell);

        return row;
    }


    function connectWebSocket() {
        socket = new WebSocket(
            "ws://localhost:8080/musashi-banking-system/customerTimelyOperations",
        );
        SocketManager.customerTimelyOperationsSocket = socket;

        socket.onopen = () => console.log("TO WebSocket connected");

        socket.onmessage = async (event) => {
            try {
                console.log(event.data);
                const data = JSON.parse(event.data);

                if (data.task === "update") {
                    // if(emptyScreen){
                        if (filters() == null) return;
                        await socket.send(filters());
                        return;
                    // }
                    // await showRefreshButton();
                    // if (typeof window.renderNotifications === "function") {
                    //     console.log("TH render notifications");
                    //     await window.renderNotifications();
                    // } else {
                    //     throw new Error("window.renderNotifications is not defined");
                    // }
                    // return;
                }

                if (data.task === "filters") {
                    if (filters() == null) return;
                    await socket.send(filters());
                    return;
                }

                if(data.task === "loadRows"){

                    loadRows(data.rows);

                    document.getElementById("totalAmount").innerHTML = "USD "+data.totalAmount;
                    document.getElementById("totalCount").innerHTML = data.resultCount;
                    document.getElementById("nextScheduled").innerHTML = data.nextDate +` <i
                      class="fas fa-circle"
                      style="font-size: 6px; line-height: 1"
                      aria-hidden="true"
              ></i> `+data.nextTime;

                }


                renderPagination(data.currentPage, data.totalPages);

                // hideRefreshButton();

                // if (typeof window.renderNotifications === "function") {
                //     await window.renderNotifications();
                // }
            } catch (err) {
                console.error("Invalid message format", err);
            }
        };

        socket.onclose = () => console.log("TO WebSocket closed");
    }

    function setFilterVariables() {
        accountNum = document.getElementById("accountNum").value;
        counterparty = document.getElementById("counterparty").value;
        scheduledStart = document.getElementById("scheduledStart").value;
        scheduledEnd = document.getElementById("scheduledEnd").value;
        createdStart = document.getElementById("createdStart").value;
        createdEnd = document.getElementById("createdEnd").value;
        status = document.getElementsByClassName('active-filter-status')[0].innerHTML;
        sortBy = document.getElementsByClassName('active-sort')[0].id;
    }

    function setFilters() {
        document.getElementById("accountNum").value = accountNum;
        document.getElementById("counterparty").value = counterparty;
        document.getElementById("scheduledStart").value = scheduledStart;
        document.getElementById("scheduledEnd").value = scheduledEnd;
        document.getElementById("createdStart").value = createdStart;
        document.getElementById("createdEnd").value = createdEnd;
        document.getElementsByClassName('active-filter-status')[0].className.replace('active', 'inactive');
        if(status === "All"){
            document.getElementById("status-all").className.replace('inactive', 'active');
        }else if (status === "Pending"){
            document.getElementById("status-pending").className.replace('inactive', 'active');
        }else if (status === "Paused"){
            document.getElementById("status-paused").className.replace('inactive', 'active');
        }
        document.getElementsByClassName('active-sort')[0].id = sortBy;
    }

    function clearFiltersAndSort() {
        document.getElementById("accountNum").value = "";
        document.getElementById("counterparty").value = "";
        document.getElementById("scheduledStart").value = "";
        document.getElementById("scheduledEnd").value = "";
        document.getElementById("createdStart").value = "";
        document.getElementById("createdEnd").value = "";
        const activeStatus = document.getElementsByClassName('active-filter-status')[0];
        activeStatus.className = activeStatus.className.replace('active', 'inactive');
        document.getElementById("status-all").className = document.getElementById("status-all").className.replace('inactive', 'active');
        const activeSort = document.getElementsByClassName('active-sort')[0];
        activeSort.className = activeSort.className.replace('active', 'inactive');
        document.getElementById("scheduledAsc").className = document.getElementById("scheduledAsc").className.replace('inactive', 'active');
    }

    function filters() {
        let output = JSON.stringify({
            accountNum,
            counterparty,
            scheduledStart,
            scheduledEnd,
            createdStart,
            createdEnd,
            status,
            page: active_page,
            sortBy,
        });

        // if (startDate && !isValidDate(startDate)) {
        //     showDateErrors("start");
        //     output = null;
        // }
        // if (endDate && !isValidDate(endDate)) {
        //     showDateErrors("end");
        //     output = null;
        // }
        return output;
    }

    async function showRefreshButton() {
        const btn = document.getElementById("refresh-btn");
        const msg = document.getElementById("refresh-msg");

        console.log(msg);

        btn.classList.add("show");
        msg.classList.add("show");

        await new Promise((resolve) => setTimeout(resolve, 3000));
        msg.classList.remove("show");

        setTimeout(showRefreshButton, 6000);
    }

    function hideRefreshButton() {
        const btn = document.getElementById("refresh-btn");
        const msg = document.getElementById("refresh-msg");

        btn.classList.remove("show");
        msg.classList.remove("show");
    }

    function setupCopyButtons() {

        console.log("copy buttons: "+ document.querySelectorAll(".copy-icon").length);

        document.querySelectorAll(".copy-icon").forEach((icon) => {
            icon.addEventListener("click", async () => {
                const textToCopy = icon.dataset.tooltip;
                await navigator.clipboard.writeText(textToCopy);

                icon.classList.remove("fa-clone");
                icon.classList.add("fa-check");

                setTimeout(() => {
                    icon.classList.remove("fa-check");
                    icon.classList.add("fa-clone");
                }, 3000);
            });
        });
    }

    function setupOverlayAndSortFilterButtons(){
        const overlay = document.getElementById("TO_overlay");

        const openFiltersBtn = document.getElementById("openFilters");
        const filterDiv = document.getElementById("filterDiv");
        const applyBtn = document.getElementById("applyBtn");
        const resetBtn = document.getElementById("resetBtn");

        const openSortBtn = document.getElementById("openSort");
        const sortDiv = document.getElementById("sortDiv");

        openFiltersBtn.addEventListener("click", () => {
            overlay.classList.add("show");
            filterDiv.classList.add("show");
        });

        openSortBtn.addEventListener("click", () => {
            overlay.classList.add("show");
            sortDiv.classList.add("show");
        });

        async function closeModal(action) {
            console.log(action + " button clicked");

            if(action === "Reset") {
                clearFiltersAndSort();
            }

            setFilterVariables();
            if (filters() == null) return;
            await socket.send(filters());
            overlay.classList.remove("show");
            filterDiv.classList.remove("show");
            sortDiv.classList.remove("show");
        }

        applyBtn.addEventListener("click", () => closeModal("Apply"));
        resetBtn.addEventListener("click", () => closeModal("Reset"));

        overlay.addEventListener("click", (e) => {
            if (e.target === overlay)
                closeModal("overlay");
        });

        //sort buttons
        const sortButtons = document.getElementById("sortDiv").querySelectorAll("button");
        sortButtons.forEach((button) => {
            button.addEventListener("click", (e) => {
                const activeSort =  document.getElementsByClassName('active-sort')[0];
                activeSort.className = activeSort.className.replace('active', 'inactive');
                button.className = button.className.replace('inactive', 'active');
                sortBy = button.id;
                closeModal(sortBy);
            })
        })

        //filter status
        const statusButtons = document.getElementById("filterStatus").querySelectorAll("button");
        statusButtons.forEach((button) => {
            button.addEventListener("click", (e) => {
                const activeStatus =  document.getElementsByClassName('active-filter-status')[0];
                activeStatus.className = activeStatus.className.replace('active', 'inactive');
                button.className = button.className.replace('inactive', 'active');
                status = button.innerText;
            })
        })
    }

    function cleanup() {
        console.log("TransferHistoryPage cleanup");

        if (midnightIntervalId !== null) {
            clearInterval(midnightIntervalId);
            midnightIntervalId = null;
        }

        if (socket && socket.readyState === WebSocket.OPEN) {
            socket.close();
        }
        socket = null;
    }

    const pageAPI = { init, cleanup };
    window.TimelyOperationsPage = pageAPI;
    return pageAPI;
})();