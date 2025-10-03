const notificationsBtn = document.getElementById("notificationsBtn");
const closeBtn = document.getElementById("closeBtn");
const divC = document.getElementById("divC");
const overlay = document.getElementById("overlay");
const divA = document.getElementById("divA");
const divB = document.getElementById("divB");

notificationsBtn.addEventListener("click", () => {
    divC.classList.add("open");
    overlay.classList.add("active");
    divA.style.pointerEvents = "none";
    divB.style.pointerEvents = "none";
});

overlay.addEventListener("click", () => {
    divC.classList.remove("open");
    overlay.classList.remove("active");
    divA.style.pointerEvents = "auto";
    divB.style.pointerEvents = "auto";
});

(async function () {
    const response = await fetch("http://localhost:8080/musashi-banking-system/loadCustomerDashboard");
    const data = await response.json();
    console.log(data);
    loadData(data);
})();

function loadData(data) {

    document.getElementById("balance").innerHTML = data.balance;
    const container = document.getElementById("status-div");

    if (data.suspended) {
        const suspendedBox = document.createElement("div");
        suspendedBox.className = "status-box suspended-status";
        suspendedBox.innerHTML = `
            <span style="font-family: bold">Suspended</span>
            <i style="font-size: 12px; margin-top: 0.3px" class="fa fa-times"></i>
        `;
        container.appendChild(suspendedBox);
    } else {
        const activeBox = document.createElement("div");
        activeBox.className = "status-box active-status";
        activeBox.innerHTML = `
            <span style="font-family: bold">Active</span>
            <i style="font-size: 12px; margin-top: 0.3px" class="fa fa-check"></i>
        `;
        container.appendChild(activeBox);
    }

    if (data.sent) {
        fillSent(data);
    }else{
        emptySent();
    }

    if (data.received) {
        fillReceived(data);
    }else{
        emptyReceived();
    }

    renderNotifications(data.notifications);

}

function fillSent(data){

    const parentDiv = document.getElementById("latest-sent-div");
    parentDiv.innerHTML = "";

    const titleSpan = document.createElement("span");
    titleSpan.classList.add("info-box-title");
    titleSpan.textContent = "Latest Sent Transfer";

    const mainVerticalDiv = document.createElement("div");
    mainVerticalDiv.classList.add("vertical-div");
    mainVerticalDiv.style.gap = "20px";

    const row1 = document.createElement("div");
    row1.classList.add("horizontal-div");
    row1.style.cssText = "margin-left: 12px; margin-right: 12px; gap: 28px; font-family: medium;";

    const accountDiv = document.createElement("div");
    const accountLabel = document.createElement("span");
    accountLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    accountLabel.textContent = "Account Number";
    const accountValue = document.createElement("span");
    accountValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    accountValue.id = "sentNumber";
    accountValue.textContent = "N/A";
    accountDiv.appendChild(accountLabel);
    accountDiv.appendChild(accountValue);

    const nameDiv = document.createElement("div");
    const nameLabel = document.createElement("span");
    nameLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    nameLabel.textContent = "Receiver's Name";
    const nameValue = document.createElement("span");
    nameValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    nameValue.id = "sentName";
    nameValue.textContent = "N/A";
    nameDiv.appendChild(nameLabel);
    nameDiv.appendChild(nameValue);

    row1.appendChild(accountDiv);
    row1.appendChild(nameDiv);

    const row2 = document.createElement("div");
    row2.classList.add("horizontal-div");
    row2.style.cssText = "margin-left: 12px; margin-right: 12px; gap: 28px; font-family: medium;";

    const amountDiv = document.createElement("div");
    const amountLabel = document.createElement("span");
    amountLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    amountLabel.textContent = "Amount";
    const amountValue = document.createElement("span");
    amountValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    amountValue.id = "sentAmount";
    amountValue.textContent = "N/A";
    amountDiv.appendChild(amountLabel);
    amountDiv.appendChild(amountValue);

    const dateDiv = document.createElement("div");
    const dateLabel = document.createElement("span");
    dateLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    dateLabel.textContent = "Date";
    const dateValue = document.createElement("span");
    dateValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    dateValue.id = "sentDate";
    dateValue.textContent = "N/A";
    dateDiv.appendChild(dateLabel);
    dateDiv.appendChild(dateValue);

    const timeDiv = document.createElement("div");
    const timeLabel = document.createElement("span");
    timeLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    timeLabel.textContent = "Time";
    const timeValue = document.createElement("span");
    timeValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    timeValue.id = "sentTime";
    timeValue.textContent = "N/A";
    timeDiv.appendChild(timeLabel);
    timeDiv.appendChild(timeValue);

    row2.appendChild(amountDiv);
    row2.appendChild(dateDiv);
    row2.appendChild(timeDiv);

    mainVerticalDiv.appendChild(row1);
    mainVerticalDiv.appendChild(row2);

    parentDiv.appendChild(titleSpan);
    parentDiv.appendChild(mainVerticalDiv);

    document.getElementById("sentNumber").innerHTML = data.sentNumber;
    document.getElementById("sentName").innerHTML = data.sentName;
    document.getElementById("sentAmount").innerHTML = data.sentAmount;
    const date = new Date(data.sentDateTime);
    document.getElementById("sentDate").innerHTML = date.toLocaleDateString();
    document.getElementById("sentTime").innerHTML = date.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false
    });
}

function fillReceived(data) {

    const parentDiv = document.getElementById("latest-received-div");
    parentDiv.innerHTML = "";

    const titleSpan = document.createElement("span");
    titleSpan.classList.add("info-box-title");
    titleSpan.textContent = "Latest Received Transfer";

    const mainVerticalDiv = document.createElement("div");
    mainVerticalDiv.classList.add("vertical-div");
    mainVerticalDiv.style.gap = "20px";

    const row1 = document.createElement("div");
    row1.classList.add("horizontal-div");
    row1.style.cssText = "margin-left: 12px; margin-right: 12px; gap: 28px; font-family: medium;";

    const accountDiv = document.createElement("div");
    const accountLabel = document.createElement("span");
    accountLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    accountLabel.textContent = "Account Number";
    const accountValue = document.createElement("span");
    accountValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    accountValue.id = "receivedNumber";
    accountDiv.appendChild(accountLabel);
    accountDiv.appendChild(accountValue);

    const nameDiv = document.createElement("div");
    const nameLabel = document.createElement("span");
    nameLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    nameLabel.textContent = "Sender's Name";
    const nameValue = document.createElement("span");
    nameValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    nameValue.id = "receivedName";
    nameDiv.appendChild(nameLabel);
    nameDiv.appendChild(nameValue);

    row1.appendChild(accountDiv);
    row1.appendChild(nameDiv);

    const row2 = document.createElement("div");
    row2.classList.add("horizontal-div");
    row2.style.cssText = "margin-left: 12px; margin-right: 12px; gap: 28px; font-family: medium;";

    const amountDiv = document.createElement("div");
    const amountLabel = document.createElement("span");
    amountLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    amountLabel.textContent = "Amount";
    const amountValue = document.createElement("span");
    amountValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    amountValue.id = "receivedAmount";
    amountDiv.appendChild(amountLabel);
    amountDiv.appendChild(amountValue);

    const dateDiv = document.createElement("div");
    const dateLabel = document.createElement("span");
    dateLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    dateLabel.textContent = "Date";
    const dateValue = document.createElement("span");
    dateValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    dateValue.id = "receivedDate";
    dateDiv.appendChild(dateLabel);
    dateDiv.appendChild(dateValue);

    const timeDiv = document.createElement("div");
    const timeLabel = document.createElement("span");
    timeLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    timeLabel.textContent = "Time";
    const timeValue = document.createElement("span");
    timeValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    timeValue.id = "receivedTime";
    timeDiv.appendChild(timeLabel);
    timeDiv.appendChild(timeValue);

    row2.appendChild(amountDiv);
    row2.appendChild(dateDiv);
    row2.appendChild(timeDiv);

    mainVerticalDiv.appendChild(row1);
    mainVerticalDiv.appendChild(row2);

    parentDiv.appendChild(titleSpan);
    parentDiv.appendChild(mainVerticalDiv);

    document.getElementById("receivedNumber").innerHTML = data.receivedNumber;
    document.getElementById("receivedName").innerHTML = data.receivedName;
    document.getElementById("receivedAmount").innerHTML = data.receivedAmount;
    const date = new Date(data.receivedDateTime);
    document.getElementById("receivedDate").innerHTML = date.toLocaleDateString();
    document.getElementById("receivedTime").innerHTML = date.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false
    });
}

function emptyReceived(){
    const parentDiv = document.getElementById("latest-received-div");
    parentDiv.innerHTML = "";

    const titleSpan = document.createElement("span");
    titleSpan.classList.add("info-box-title");
    titleSpan.textContent = "Latest Received Transfer";

    const innerDiv = document.createElement("div");
    innerDiv.classList.add("center-div");
    innerDiv.style.cssText = `
    font-family: medium;
    gap: 10px;
    height: 60%;
    color: #727272;
`;

    const img = document.createElement("img");
    img.src = "/musashi-banking-system/images/no-results.png";
    img.width = 30;
    img.height = 30;
    img.alt = "";
    img.style.filter = "invert(0.25)";

    const messageSpan = document.createElement("span");
    messageSpan.textContent = "No Incoming Transfers Found";

    innerDiv.appendChild(img);
    innerDiv.appendChild(messageSpan);

    parentDiv.appendChild(titleSpan);
    parentDiv.appendChild(innerDiv);

}

function emptySent(){
    const parentDiv = document.getElementById("latest-sent-div");
    parentDiv.innerHTML = "";

    const titleSpan = document.createElement("span");
    titleSpan.classList.add("info-box-title");
    titleSpan.textContent = "Latest Sent Transfer";

    const innerDiv = document.createElement("div");
    innerDiv.classList.add("center-div");
    innerDiv.style.cssText = `
    font-family: medium;
    gap: 10px;
    height: 100%;
    color: #727272;
`;

    const img = document.createElement("img");
    img.src = "/musashi-banking-system/images/no-results.png";
    img.width = 30;
    img.height = 30;
    img.alt = "";
    img.style.filter = "invert(0.25)";

    const messageSpan = document.createElement("span");
    messageSpan.textContent = "No Outgoing Transfers Found";

    innerDiv.appendChild(img);
    innerDiv.appendChild(messageSpan);

    parentDiv.appendChild(titleSpan);
    parentDiv.appendChild(innerDiv);

}

(function () {
    const input = document.getElementById("dateInput");

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

    input.addEventListener("paste", (e) => {
        e.preventDefault();
        const text = (e.clipboardData || window.clipboardData).getData("text");
        const digits = text.replace(/\D/g, "").slice(0, 8);
        input.value = formatDate(digits);
    });
})();


(function () {
    const input = document.getElementById("timeInput");

    function formatTime(digits) {
        let out = "";

        if (digits.length > 0) {
            out += digits.slice(0, Math.min(2, digits.length)); // HH

            if (digits.length >= 2) {
                out += ":"; // add colon only after HH complete
            }

            if (digits.length > 2) {
                out += digits.slice(2, Math.min(4, digits.length)); // MM
            }
        }

        return out;
    }

    input.addEventListener("input", () => {
        const raw = input.value;
        const digits = raw.replace(/\D/g, "").slice(0, 4);
        input.value = formatTime(digits);
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
        e.preventDefault(); // block letters & symbols
    });

    input.addEventListener("paste", (e) => {
        e.preventDefault();
        const text = (e.clipboardData || window.clipboardData).getData("text");
        const digits = text.replace(/\D/g, "").slice(0, 4);
        input.value = formatTime(digits);
    });

    // Optional: basic validation on blur
    // input.addEventListener("blur", () => {
    //     const val = input.value;
    //     if (/^\d{2}:\d{2}$/.test(val)) {
    //         let [h, m] = val.split(":").map(Number);
    //         if (h > 23 || m > 59) {
    //             input.style.borderColor = "#dc2626"; // red if invalid
    //         } else {
    //             input.style.borderColor = ""; // reset
    //         }
    //     } else if (val !== "") {
    //         input.style.borderColor = "#dc2626";
    //     }
    // });
})();

function formatNotificationTime(dateStr) {
    const dt = new Date(dateStr);
    const now = new Date();

    const isToday =
        dt.getDate() === now.getDate() &&
        dt.getMonth() === now.getMonth() &&
        dt.getFullYear() === now.getFullYear();

    const yesterday = new Date(now);
    yesterday.setDate(now.getDate() - 1);

    const isYesterday =
        dt.getDate() === yesterday.getDate() &&
        dt.getMonth() === yesterday.getMonth() &&
        dt.getFullYear() === yesterday.getFullYear();

    const optionsTime = { hour: "numeric", minute: "2-digit" };

    if (isToday) {
        return dt.toLocaleTimeString([], optionsTime);
    } else if (isYesterday) {
        return "Yesterday " + dt.toLocaleTimeString([], optionsTime);
    } else {
        let optionsDate;
        if (dt.getFullYear() < now.getFullYear()) {
            // include year if it's before current year
            optionsDate = { day: "numeric", month: "long", year: "numeric" };
        } else {
            optionsDate = { day: "numeric", month: "long" };
        }

        return (
            dt.toLocaleDateString([], optionsDate) +
            ", " +
            dt.toLocaleTimeString([], optionsTime)
        );
    }
}

function renderNotifications(data) {
    const newContainer = document.createElement("div");
    newContainer.style.display = "flex";
    newContainer.style.flexDirection = "column";
    newContainer.style.gap = "14px";
    const oldContainer = document.createElement("div");
    oldContainer.style.display = "flex";
    oldContainer.style.flexDirection = "column";
    oldContainer.style.gap = "14px";

    // Add headings
    const newHeading = document.createElement("span");
    newHeading.textContent = "New";
    newHeading.style.fontFamily = "medium";
    newHeading.style.fontSize = "12px";
    newHeading.style.color = "#727272";

    const oldHeading = document.createElement("span");
    oldHeading.textContent = "Old";
    oldHeading.style.fontFamily = "medium";
    oldHeading.style.fontSize = "12px";
    oldHeading.style.color = "#727272";

    // Sort by ID descending (latest first)
    const notifications = data.sort((a, b) => b.id - a.id);

    notifications.forEach((n) => {
        const notifDiv = document.createElement("div");
        notifDiv.classList.add("notification");

        const msg = document.createElement("span");
        msg.textContent = n.message;

        const time = document.createElement("span");
        time.classList.add("notification-time");
        time.textContent = formatNotificationTime(n.dateTime);

        notifDiv.appendChild(msg);
        notifDiv.appendChild(time);

        if (n.status === "SENT") {
            newContainer.appendChild(notifDiv);
        } else {
            oldContainer.appendChild(notifDiv);
        }
    });

    const divC = document.getElementById("divC");
    divC.innerHTML = ""; // clear sample data

    if (newContainer.hasChildNodes()) {
        divC.appendChild(newHeading);
        divC.appendChild(newContainer);
    }

    if (oldContainer.hasChildNodes()) {
        divC.appendChild(oldHeading);
        divC.appendChild(oldContainer);
    }
}

const checkbox = document.getElementById("transferLaterCheckbox");
const dateInput = document.getElementById("dateInput");
const timeInput = document.getElementById("timeInput");

checkbox.addEventListener("change", () => {
    dateInput.value = "";
    timeInput.value = "";
    const enabled = checkbox.checked;
    if(enabled) {
        dateInput.disabled = false;
        timeInput.disabled = false;
        dateInput.style.cursor = "default";
        timeInput.style.cursor = "default";
    }else{
        dateInput.disabled = true;
        timeInput.disabled = true;
        dateInput.style.cursor = "not-allowed";
        timeInput.style.cursor = "not-allowed";
    }
});

function submit(){

}