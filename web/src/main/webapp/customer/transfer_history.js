(() => {
    if (window.TransferHistoryPage) {
        return window.TransferHistoryPage;
    }

    let active_page = 1;
    let sort = "dateDesc";
    let socket = null;

    let accountNum = "";
    let counterparty = "";
    let startDate = "";
    let endDate = "";
    let type = "0";
    let emptyScreen = false;

    function init() {
        console.log("TransferHistoryPage initialized");

        setupResetButton();
        setupCustomSelects();
        setupDateInputs();
        connectWebSocket();
        setupSortingAmountBtn();
        setupSortingDateBtn();
        setupRefreshButton();
        setupApplyButton();
        setFilters();

        // empty-message → go to dashboard and clean up current page
        const emptyBtn = document.getElementById("empty-message")?.querySelector("button");
        if (emptyBtn) {
            emptyBtn.addEventListener("click", () => {
                document.getElementById("dashboard")?.click();
            });
        }
    }

    function showEmptyScreen(){

        if(emptyScreen){
            return;
        }

        emptyScreen = !emptyScreen;

        const tableTop = document.getElementsByClassName("table-top")[0];
        tableTop.style.display = "none";

        const tableBottom = document.getElementsByClassName("table-bottom")[0];
        tableBottom.style.display = "none";

        const rows = document.getElementById("rows");
        rows.style.display = "none";

        document.getElementById("empty-message").style.display = "flex";
    }

    function hideEmptyScreen(){

        if(!emptyScreen){
            return;
        }

        emptyScreen = !emptyScreen;

        const tableTop = document.getElementsByClassName("table-top")[0];
        tableTop.style.display = "flex";

        const tableBottom = document.getElementsByClassName("table-bottom")[0];
        tableBottom.style.display = "flex";

        const rows = document.getElementById("rows");
        rows.style.display = "block";

        document.getElementById("empty-message").style.display = "none";
    }

    function setFilterVariables() {
        accountNum = document.getElementById("account-num").value;
        counterparty = document.getElementById("counterparty").value;
        type = document.getElementById("type").dataset.value;
        startDate = document.getElementById("startDate").value;
        endDate = document.getElementById("endDate").value;
    }

    function clearFilterVariables() {
        accountNum = ""
        counterparty = "";
        type = "0";
        startDate = "";
        endDate = "";
        emptyScreen = false;
    }

    function setFilters() {
        document.getElementById("account-num").value = accountNum;
        document.getElementById("counterparty").value = counterparty;
        document.getElementById("startDate").value = startDate;
        if (startDate === "") {
            document.getElementById("startDate").style.borderColor = "#0d0d0d46";
        }
        document.getElementById("endDate").value = endDate;
        if (endDate === "") {
            document.getElementById("endDate").style.borderColor = "#0d0d0d46";
        }

        if (type === "0") {
            document.querySelector('.custom-option[data-value="0"]').click();
            console.log("0 clicked");
        } else if (type === "1") {

            document.querySelector('.custom-option[data-value="1"]').click();
            console.log("1 clicked");

        } else if (type === "2") {

            document.querySelector('.custom-option[data-value="2"]').click();
            console.log("2 clicked");

        }
    }

    function setupApplyButton() {

        const button = document.getElementById("apply");
        button.addEventListener("click", async (e) => {
            active_page = 1;
            setFilterVariables();
            if (filters() == null) return;
            await socket.send(filters());
        })
    }

    async function showRefreshButton() {
        const btn = document.querySelector(".refresh-btn");
        const msg = document.querySelector(".refresh-msg");
        if (!btn.classList.contains("show")) {
            btn.disabled = false;
            btn.classList.add("show");
            msg.classList.add("show");
            await new Promise(resolve => setTimeout(resolve, 3000));
            msg.classList.remove("show");
        }

    }

    function hideRefreshButton() {
        const btn = document.querySelector(".refresh-btn");
        const msg = document.querySelector(".refresh-msg");
        if (btn.classList.contains("show")) {
            btn.disabled = true;
            btn.classList.remove("show");
            if (msg.classList.contains("show")) {
                msg.classList.remove("show");
            }
        }
    }

    function setupRefreshButton() {
        const btn = document.querySelector(".refresh-btn");
        btn.disabled = true;
        btn.addEventListener("click", async () => {
            if (filters() == null) return;
            await socket.send(filters());
        });
    }

    function setupSortingDateBtn() {
        document.getElementById("sortDateBtn").addEventListener("click", () => {
            document.getElementById("sortAmount").innerText = "MIXED";
            let sortBy = document.getElementById("sortDate");
            if (sortBy.innerText === "DESC") {
                sortBy.innerText = "ASC";
                sort = "dateAsc";
            } else if (sortBy.innerText === "ASC") {
                sortBy.innerText = "DESC";
                sort = "dateDesc";
            } else {
                sortBy.innerText = "DESC";
                sort = "dateDesc";
            }
        });

    }

    function setupSortingAmountBtn() {
        document.getElementById("sortAmountBtn").addEventListener("click", () => {
            document.getElementById("sortDate").innerText = "MIXED";
            let sortBy = document.getElementById("sortAmount");
            if (sortBy.innerText === "DESC") {
                sortBy.innerText = "ASC";
                sort = "amountAsc";
            } else if (sortBy.innerText === "ASC") {
                sortBy.innerText = "DESC";
                sort = "amountDesc";
            } else {
                sortBy.innerText = "DESC";
                sort = "amountDesc";
            }
        });
    }

    function cleanup() {
        console.log("TransferHistoryPage cleanup");
        clearFilterVariables();
        if (socket && socket.readyState === WebSocket.OPEN) socket.close();
        socket = null;
    }

    async function reset(){
        const accountNum = document.getElementById("account-num");
        const counterparty = document.getElementById("counterparty");
        const startDate = document.getElementById("startDate");
        const endDate = document.getElementById("endDate");

        accountNum.value = "";
        counterparty.value = "";
        document.querySelector('.custom-option[data-value="0"]').click();
        startDate.value = "";
        startDate.style.borderColor = "#0d0d0d46";
        endDate.value = "";
        endDate.style.borderColor = "#0d0d0d46";
        emptyScreen = false;

        document.getElementById("sortDate").innerText = "DESC";
        document.getElementById("sortAmount").innerText = "MIXED";
        sort = "dateDesc";

        setFilterVariables();

        if (socket && socket.readyState === WebSocket.OPEN) {
            if (filters() == null) return;
            await socket.send(filters());
        }
    }

    function setupResetButton() {
        const resetBtn = document.getElementById("reset");
        if (!resetBtn) return;
        resetBtn.addEventListener("click", reset);
    }

    function setupCustomSelects() {
        document.querySelectorAll(".custom-select").forEach((select) => {
            const trigger = select.querySelector(".custom-select-trigger");
            const options = select.querySelectorAll(".custom-option");
            const textSpan = trigger.querySelector("span");

            trigger.addEventListener("click", () => {
                document.querySelectorAll(".custom-select").forEach((s) => {
                    if (s !== select) s.classList.remove("open");
                });
                select.classList.toggle("open");
            });

            options.forEach((option) => {
                option.addEventListener("click", () => {
                    options.forEach((o) => o.classList.remove("selected"));
                    option.classList.add("selected");
                    textSpan.textContent = option.textContent;
                    trigger.dataset.value = option.dataset.value;
                    select.classList.remove("open");

                    if (option.dataset.value === "0") {
                        trigger.classList.remove("active");
                    } else {
                        trigger.classList.add("active");
                    }
                });
            });
        });

        document.addEventListener("click", (e) => {
            if (!e.target.closest(".custom-select")) {
                document
                    .querySelectorAll(".custom-select")
                    .forEach((s) => s.classList.remove("open"));
            }
        });
    }

    function setupDateInputs() {
        const inputs = document.querySelectorAll(".dateInput");

        function formatDate(digits) {
            const n = digits.length;
            let out = "";
            if (n > 0) {
                out += digits.slice(0, Math.min(2, n));
                if (n >= 2) out += "/";
                if (n > 2) {
                    out += digits.slice(2, Math.min(4, n));
                    if (n >= 4) out += "/";
                }
                if (n > 4) out += digits.slice(4, Math.min(8, n));
            }
            return out;
        }

        inputs.forEach((input) => {
            input.addEventListener("input", () => {
                const raw = input.value;
                const digits = raw.replace(/\D/g, "").slice(0, 8);
                input.value = formatDate(digits);
            });

            input.addEventListener("keydown", (e) => {
                const allowed = [
                    "Backspace",
                    "Delete",
                    "ArrowLeft",
                    "ArrowRight",
                    "Tab",
                    "Home",
                    "End",
                ];
                if (allowed.includes(e.key)) return;
                if (/^[0-9]$/.test(e.key)) return;
                e.preventDefault();
            });

            input.addEventListener("blur", () => {
                const value = input.value.trim();
                const dateError =
                    input.id === "startDate"
                        ? document.getElementById("startDateError")
                        : document.getElementById("endDateError");

                if (value === "") {
                    dateError.classList.remove("show");
                    input.style.borderColor = "#0d0d0d46";
                } else {
                    input.style.borderColor = "#007bff";
                }
            });

            input.addEventListener("click", () => {
                hideDateErrors(input.id === "startDate" ? "start" : "end");
            });
        });
    }

    function setupCopyButtons() {
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

    function connectWebSocket() {
        socket = new WebSocket(
            "ws://localhost:8080/musashi-banking-system/customerTransactionHistory"
        );
        SocketManager.customerTransferHistorySocket = socket;

        socket.onopen = () => console.log("TH WebSocket connected");

        socket.onmessage = async (event) => {
            try {
                const data = JSON.parse(event.data);
                console.log(event.data);
                if (data.task === "update") {
                    if(emptyScreen){
                        if (filters() == null) return;
                        await socket.send(filters());
                        return;
                    }
                    await showRefreshButton();
                    if (typeof window.renderNotifications === "function") {
                        console.log("TH render notifications");
                        await window.renderNotifications();
                    } else {
                        throw new Error("window.renderNotifications is not defined");
                    }
                    return;
                }

                if (data.task === "filters") {
                    if (filters() == null) return;
                    await socket.send(filters());
                    return;
                }

                loadData(data.rows);
                renderPagination(data.currentPage, data.totalPages);

                hideRefreshButton();

                if (typeof window.renderNotifications === "function") {
                    await window.renderNotifications();
                }
            } catch (err) {
                console.error("Invalid message format", err);
            }
        };

        socket.onclose = () => console.log("TH WebSocket closed");
    }

    function renderPagination(currentPage, totalPages) {
        const container = document.querySelector(".table-bottom");
        container.innerHTML = "";

        if (currentPage !== 1) {
            const prev = document.createElement("button");
            prev.classList.add("prev");
            prev.innerHTML = `<i class="fa fa-arrow-left"></i>`;
            prev.onclick = () => loadTHPage(currentPage - 1);
            container.appendChild(prev);
        }

        if(totalPages > 1) {
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

    function loadTHPage(page) {
        active_page = page || 1;
        setFilters();
        if (socket && socket.readyState === WebSocket.OPEN)
            socket.send(filters());
    }

    function isValidDate(dateStr) {
        const regex = /^(\d{2})\/(\d{2})\/(\d{4})$/;
        const match = dateStr.match(regex);
        if (!match) return false;
        const [_, dd, mm, yyyy] = match;
        const d = new Date(yyyy, mm - 1, dd);
        return (
            d.getFullYear() == yyyy &&
            d.getMonth() == mm - 1 &&
            d.getDate() == dd
        );
    }

    function filters() {

        let output = JSON.stringify({
            accountNum,
            counterparty,
            startDate,
            endDate,
            type,
            sort,
            page: active_page,
        });

        if (startDate && !isValidDate(startDate)) {
            showDateErrors("start");
            output = null;
        }
        if (endDate && !isValidDate(endDate)) {
            showDateErrors("end");
            output = null;
        }
        return output;
    }

    function loadData(rows) {

        if(rows && rows.length === 0) {
            console.log("No rows found");
            showEmptyScreen();
            return;
        }

        hideEmptyScreen();
        const rowsContainer = document.getElementById("rows");
        rowsContainer.style.display = "none";
        rowsContainer.innerHTML = "";

        rows.forEach((row) => {
            const div = document.createElement("div");
            div.className = "horizontal-div table-row";

            const col1 = document.createElement("div");
            col1.className = "table-row-div";

            const span = document.createElement("span");

            span.style.display = "inline-flex";
            span.style.alignItems = "center";
            span.style.gap = "8px";

            span.innerHTML = row.datetime.replace(
                ",",
                ' <i class="fa fa-circle" aria-hidden="true"></i> '
            );

            const icon = span.querySelector("i");
            if (icon) {
                icon.style.fontSize = "4px";
                icon.style.lineHeight = "1";
                icon.style.display = "inline-block";
            }

            col1.appendChild(span);
            div.appendChild(col1);


            const accountNumber = row.accountNumber;
            const col2 = document.createElement("div");
            col2.className = "table-row-div";
            col2.innerHTML = `
                <i class="fa fa-clone copy-icon" data-tooltip="${accountNumber}"></i>
                <span><span class="mask">******</span>${accountNumber.slice(-3)}</span>`;
            div.appendChild(col2);

            const col3 = document.createElement("div");
            col3.className = "table-row-div";
            col3.innerHTML = `<span>${row.counterparty}</span>`;
            div.appendChild(col3);

            const col4 = document.createElement("div");
            col4.className = "table-row-div";
            if (row.type === "sent") {
                col4.innerHTML = `<div class="amount outgoing">− ${row.amount}</div>`;
            } else {
                col4.innerHTML = `<div class="amount incoming">+ ${row.amount}</div>`;
            }
            div.appendChild(col4);

            const col5 = document.createElement("div");
            col5.className = "table-row-div";
            const id = row.id;
            col5.innerHTML = `
                <i class="fa fa-clone copy-icon" data-tooltip="${id}"></i>
                <span>${id.slice(0, 5)}<span class="mask">**********</span>${id.slice(-5)}</span>`;
            div.appendChild(col5);

            rowsContainer.appendChild(div);
        });

        rowsContainer.style.display = "block";
        setupCopyButtons();
    }

    function showDateErrors(date) {
        if (date === "start") {
            document.getElementById("startDateError").classList.add("show");
            document.getElementById("startDate").style.borderColor = "#d8000c";
        } else if (date === "end") {
            document.getElementById("endDateError").classList.add("show");
            document.getElementById("endDate").style.borderColor = "#d8000c";
        }
    }

    function hideDateErrors(date) {
        if (date === "start") {
            document.getElementById("startDateError").classList.remove("show");
            document.getElementById("startDate").style.borderColor = "#0d0d0d46";
        } else if (date === "end") {
            document.getElementById("endDateError").classList.remove("show");
            document.getElementById("endDate").style.borderColor = "#0d0d0d46";
        }
    }

    const pageAPI = { init, cleanup };
    window.TransferHistoryPage = pageAPI;
    return pageAPI;
})();