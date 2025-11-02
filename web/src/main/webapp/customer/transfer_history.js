(function () {
    const resetBtn = document.getElementById("reset");
    resetBtn.addEventListener("click", () => {
        const accountNum = document.getElementById("account-num");
        const counterparty = document.getElementById("counterparty");
        const startDate = document.getElementById("startDate");
        const endDate = document.getElementById("endDate");
        const options = document.querySelectorAll(".custom-option");
        const select = document.getElementById("type-select");
        const trigger = select.querySelector(".custom-select-trigger");
        const textSpan = trigger.querySelector("span");
        const defaultOption = select.querySelector(
            '.custom-option[data-value="0"]'
        );
        options.forEach((option) => {
            option.classList.remove("selected");
        });

        accountNum.value = "";
        counterparty.value = "";
        options.forEach((o) => o.classList.remove("selected"));
        defaultOption.classList.add("selected");
        textSpan.textContent = defaultOption.textContent;
        trigger.dataset.value = defaultOption.dataset.value;
        trigger.classList.remove("active");
        select.classList.remove("open");
        startDate.value = "";
        startDate.style.borderColor = "#0d0d0d46";
        endDate.value = "";
        endDate.style.borderColor = "#0d0d0d46";

        document.getElementById("sortDate").innerText = "DESC";
        document.getElementById("sortAmount").innerText = "MIXED";
        sort = "dateDesc";

        SocketManager.customerTransferHistorySocket.send("");
    });
})();

document.querySelectorAll(".custom-select").forEach((select) => {
    const trigger = select.querySelector(".custom-select-trigger");
    const options = select.querySelectorAll(".custom-option");

    // ✅ Get the <span> inside trigger (we'll update only this)
    const textSpan = trigger.querySelector("span");

    // open/close dropdown
    trigger.addEventListener("click", () => {
        document.querySelectorAll(".custom-select").forEach((s) => {
            if (s !== select) s.classList.remove("open");
        });
        select.classList.toggle("open");
    });

    // option click
    options.forEach((option) => {
        option.addEventListener("click", () => {
            options.forEach((o) => o.classList.remove("selected"));
            option.classList.add("selected");

            // ✅ Update only the text content (not the whole trigger)
            textSpan.textContent = option.textContent;

            trigger.dataset.value = option.dataset.value;
            select.classList.remove("open");

            // 🔹 Update border color depending on value
            if (option.dataset.value === "0") {
                trigger.classList.remove("active"); // grey
            } else {
                trigger.classList.add("active"); // blue
            }
        });
    });
});

// close dropdown when clicking outside
document.addEventListener("click", (e) => {
    if (!e.target.closest(".custom-select")) {
        document
            .querySelectorAll(".custom-select")
            .forEach((s) => s.classList.remove("open"));
    }
});

(function () {
    const inputs = document.querySelectorAll(".dateInput");

    function formatDate(digits) {
        const n = digits.length;
        let out = "";

        if (n > 0) {
            // day (DD)
            out += digits.slice(0, Math.min(2, n));
            // add slash after day as soon as day is complete (n >= 2)
            if (n >= 2) out += "/";

            // month (MM)
            if (n > 2) {
                out += digits.slice(2, Math.min(4, n));
                // add slash after month as soon as month is complete (n >= 4)
                if (n >= 4) out += "/";
            }

            // year (YYYY)
            if (n > 4) {
                out += digits.slice(4, Math.min(8, n));
            }
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
            e.preventDefault(); // block letters/symbols
        });

        // input.addEventListener("paste", (e) => {
        //   e.preventDefault();
        //   const text = (e.clipboardData || window.clipboardData).getData(
        //     "text"
        //   );
        //   const digits = text.replace(/\D/g, "").slice(0, 8);
        //   input.value = formatDate(digits);
        // });

        input.addEventListener("blur", () => {
            const value = input.value.trim();

            if(input.id === "startDate"){
                dateError = document.getElementById("startDateError");
            }else{
                dateError = document.getElementById("endDateError");
            }

            if (value === "") {
                dateError.classList.remove("show");
                input.style.borderColor = "#0d0d0d46";
            }else{
                input.style.borderColor = "#007bff";
            }

            // if (!isValidDate(value)) {
            //     dateError.classList.add("show");
            //     input.style.borderColor = "#d8000c";
            // } else {
            //     dateError.classList.remove("show");
            //     input.style.borderColor = "#007bff";
            // }
        });

        input.addEventListener("click", () => {
            if(input.id === "startDate"){
                hideDateErrors("start");
            }else{
                hideDateErrors("end");
            }
        });
    });
})();

document.querySelectorAll(".copy-icon").forEach((icon) => {
    icon.addEventListener("click", () => {
        // optional: copy nearby text to clipboard
        // const textToCopy = icon.nextElementSibling.textContent.trim();
        // navigator.clipboard.writeText(textToCopy);

        // change icon and tooltip
        icon.classList.remove("fa-clone");
        icon.classList.add("fa-check");

        // revert after 3s
        setTimeout(() => {
            icon.classList.remove("fa-check");
            icon.classList.add("fa-clone");
        }, 3000);
    });
});

SocketManager.customerTransferHistorySocket = new WebSocket("ws://localhost:8080/musashi-banking-system/customerTransactionHistory");

SocketManager.customerTransferHistorySocket.onopen = () => console.log("TH WebSocket connected");

SocketManager.customerTransferHistorySocket.onmessage = async event => {
    // console.log(event.data);
    try {

        const data = JSON.parse(event.data);

        if (data.task ==="update"){
            if(filters()==null) return;
            await SocketManager.customerTransferHistorySocket.send(filters());
            return;
        }
        loadData(data.rows);

        // const data = JSON.parse(event.data);
        // console.log(data);
        // loadData(data);
        if (typeof window.renderNotifications === "function") {
            await window.renderNotifications();
        } else {
            throw new Error("window.renderNotifications is not defined");
        }
    } catch (err) {
        console.error("Invalid message format", err);
    }
};

SocketManager.customerTransferHistorySocket.onclose = () => console.log("TH WebSocket closed");

function isValidDate(dateStr) {
    const regex = /^(\d{2})\/(\d{2})\/(\d{4})$/;
    const match = dateStr.match(regex);
    if (!match) return false;

    const day = parseInt(match[1]);
    const month = parseInt(match[2]);
    const year = parseInt(match[3]);

    const date = new Date(year, month - 1, day);
    return (
        date.getFullYear() === year &&
        date.getMonth() === month - 1 &&
        date.getDate() === day
    );
}

function filters() {

    const accountNum = document.getElementById("account-num").value;
    const counterparty = document.getElementById("counterparty").value;
    const type = document.getElementById("type").dataset.value;
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;

    let output = JSON.stringify({
        accountNum: accountNum,
        counterparty: counterparty,
        startDate: startDate,
        endDate: endDate,
        type: type,
        sort: sort,
    });

    if(startDate && !isValidDate(startDate)) {
        showDateErrors("start");
        output = null;
    }
    if(endDate && !isValidDate(endDate)) {
        showDateErrors("end");
        output = null;
    }

    return output;
}

function loadData(rows) {
    const rowsContainer = document.getElementById("rows");
    rowsContainer.style.display = "none";
    rowsContainer.innerHTML = "";

    // const rows = JSON.parse(data);
    rows.forEach((row) => {
        const div = document.createElement("div");
        div.className = "horizontal-div table-row";

        // --- Column 1: Date/Time ---
        const col1 = document.createElement("div");
        col1.className = "table-row-div";
        col1.innerHTML = `<span>${row.datetime}</span>`;
        div.appendChild(col1);

        // --- Column 2: Account / Copy icon ---
        const accountNumber = row.accountNumber;
        const col2 = document.createElement("div");
        col2.className = "table-row-div";
        col2.innerHTML = `
    <i class="fa fa-clone copy-icon" aria-hidden="true" data-tooltip="${accountNumber}"></i>
    <span><span class="mask">******</span>${accountNumber.slice(-3)}</span>
  `;
        div.appendChild(col2);

        // --- Column 3: Counterparty ---
        const col3 = document.createElement("div");
        col3.className = "table-row-div";
        col3.innerHTML = `<span>${row.counterparty}</span>`;
        div.appendChild(col3);

        // --- Column 4: Amount ---
        const col4 = document.createElement("div");
        col4.className = "table-row-div";
        if(row.type === "sent"){
            col4.innerHTML = `<div class="amount outgoing">− ${row.amount}</div>`;
        }else{
            col4.innerHTML = `<div class="amount incoming">+ ${row.amount}</div>`;
        }

        div.appendChild(col4);

        // --- Column 5: Transaction ID ---
        const col5 = document.createElement("div");
        col5.className = "table-row-div";
        const id = row.id;
        col5.innerHTML = `
    <i class="fa fa-clone copy-icon" aria-hidden="true" data-tooltip="${id}"></i>
    <span>${id.slice(0, 5)}<span class="mask">**********</span>${id.slice(-5)}</span>
  `;
        div.appendChild(col5);

        // append the whole row
        rowsContainer.appendChild(div);
    });

    rowsContainer.style.display = "block";
    copyButtons();
}

function copyButtons(){

    document.querySelectorAll(".copy-icon").forEach((icon) => {
        icon.addEventListener("click", async () => {

            const textToCopy = icon.dataset.tooltip;
            await navigator.clipboard.writeText(textToCopy);

            // change icon and tooltip
            icon.classList.remove("fa-clone");
            icon.classList.add("fa-check");

            // revert after 3s
            setTimeout(() => {
                icon.classList.remove("fa-check");
                icon.classList.add("fa-clone");
            }, 3000);
        });
    });
}

function showDateErrors(date){

    if (date === "start"){
        document.getElementById("startDateError").classList.add("show");
        document.getElementById("startDate").style.borderColor = "#d8000c";
    }else if (date === "end"){
        document.getElementById("endDateError").classList.add("show");
        document.getElementById("endDate").style.borderColor = "#d8000c";
    }
}

function hideDateErrors(date){
    if (date === "start"){
        document.getElementById("startDateError").classList.remove("show");
        document.getElementById("startDate").style.borderColor = "#0d0d0d46";
    }else if (date === "end"){
        document.getElementById("endDateError").classList.remove("show");
        document.getElementById("endDate").style.borderColor = "#0d0d0d46";
    }
}

let sort = "dateDesc";

function sortByDate(){

    document.getElementById("sortAmount").innerText = "MIXED";
    let sortBy = document.getElementById("sortDate");

    if (sortBy.innerText === "DESC"){
        sortBy.innerText = "ASC";
        sort = "dateAsc";
    }else if (sortBy.innerText === "ASC"){
        sortBy.innerText = "DESC";
        sort = "dateDesc";
    }else{
        sortBy.innerText = "DESC";
        sort = "dateDesc";
    }
}

function sortByAmount(){
    document.getElementById("sortDate").innerText = "MIXED";
    let sortBy = document.getElementById("sortAmount");

    if (sortBy.innerText === "DESC"){
        sortBy.innerText = "ASC";
        sort = "amountAsc";
    }else if (sortBy.innerText === "ASC"){
        sortBy.innerText = "DESC";
        sort = "amountDesc";
    }else{
        sortBy.innerText = "DESC";
        sort = "amountDesc";
    }
}

