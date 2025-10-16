// window.sendRequest = async function () {
//     const response = await fetch(`${window.CONTEXT_PATH}/loadCustomerDashboard`);
//     const data = await response.json();
//     console.log(data);
//     loadData(data);
//     if (typeof window.renderNotifications === "function") {
//         await window.renderNotifications();
//     } else {
//         throw new Error("window.renderNotifications is not defined");
//     }
// }

function loadData(data) {

    document.getElementById("balance").innerHTML = data.balance;
    const container = document.getElementById("status-div");
    container.innerHTML = "";

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
    } else {
        emptySent();
    }

    if (data.received) {
        fillReceived(data);
    } else {
        emptyReceived();
    }

}

function fillSent(data) {

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
    accountValue.style.cursor = "pointer";
    accountValue.id = "sentNumber";
    accountDiv.appendChild(accountLabel);
    accountDiv.appendChild(accountValue);

    const nameDiv = document.createElement("div");
    const nameLabel = document.createElement("span");
    nameLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    nameLabel.textContent = "Receiver's Name";
    const nameValue = document.createElement("span");
    nameValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    nameValue.id = "sentName";
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
    amountDiv.appendChild(amountLabel);
    amountDiv.appendChild(amountValue);

    const dateDiv = document.createElement("div");
    const dateLabel = document.createElement("span");
    dateLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    dateLabel.textContent = "Date";
    const dateValue = document.createElement("span");
    dateValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    dateValue.id = "sentDate";
    dateDiv.appendChild(dateLabel);
    dateDiv.appendChild(dateValue);

    const timeDiv = document.createElement("div");
    const timeLabel = document.createElement("span");
    timeLabel.style.cssText = "margin-left: 4px; font-size: 15px; color: #6c757d";
    timeLabel.textContent = "Time";
    const timeValue = document.createElement("span");
    timeValue.style.cssText = "margin-left: 4px; font-size: 18px; color: #000000";
    timeValue.id = "sentTime";
    timeDiv.appendChild(timeLabel);
    timeDiv.appendChild(timeValue);

    row2.appendChild(amountDiv);
    row2.appendChild(dateDiv);
    row2.appendChild(timeDiv);

    mainVerticalDiv.appendChild(row1);
    mainVerticalDiv.appendChild(row2);

    parentDiv.appendChild(titleSpan);
    parentDiv.appendChild(mainVerticalDiv);

    const accountNum = document.getElementById("sentNumber");
    accountNum.dataset.full = data.sentNumber;
    accountNum.innerHTML = data.sentNumber;
    mask(accountNum);

    document.getElementById("sentName").innerHTML = data.sentName;
    document.getElementById("sentAmount").innerHTML = data.sentAmount;
    const date = new Date(data.sentDateTime);
    document.getElementById("sentDate").innerHTML = date.toLocaleDateString("en-US", {
        month: "short", // short month name → "Oct"
        day: "numeric", // day number → "4"
        year: "numeric" // year → "2025"
    });
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
    accountValue.style.cursor = "pointer";
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

    const accountNum = document.getElementById("receivedNumber");
    accountNum.dataset.full = data.receivedNumber;
    accountNum.innerHTML = data.receivedNumber;
    mask(accountNum);
    document.getElementById("receivedName").innerHTML = data.receivedName;
    document.getElementById("receivedAmount").innerHTML = data.receivedAmount;
    const date = new Date(data.receivedDateTime);
    document.getElementById("receivedDate").innerHTML = date.toLocaleDateString("en-US", {
        month: "short", // short month name → "Oct"
        day: "numeric", // day number → "4"
        year: "numeric" // year → "2025"
    });
    document.getElementById("receivedTime").innerHTML = date.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false
    });
}

function emptyReceived() {
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
    img.src = `${window.CONTEXT_PATH}/images/no-results.png`;
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

function emptySent() {
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
    img.src = `${window.CONTEXT_PATH}/images/no-results.png`;
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
    const amountInput = document.getElementById("amount");

    amountInput.addEventListener("input", function () {
        let value = this.value;

        // Remove all characters except digits and one dot
        value = value.replace(/[^0-9.]/g, "");

        // Only allow one dot
        if ((value.match(/\./g) || []).length > 1) {
            value = value.slice(0, value.lastIndexOf("."));
        }

        // Limit to two decimal places
        if (value.includes(".")) {
            const [integerPart, decimalPart] = value.split(".");
            value = integerPart + "." + decimalPart.slice(0, 2);
        }

        this.value = value;
    });
})();

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

})();

const checkbox = document.getElementById("transferLaterCheckbox");
const dateInput = document.getElementById("dateInput");
const timeInput = document.getElementById("timeInput");

checkbox.addEventListener("change", () => {
    dateInput.value = "";
    timeInput.value = "";
    const enabled = checkbox.checked;
    if (enabled) {
        dateInput.disabled = false;
        timeInput.disabled = false;
        dateInput.style.cursor = "default";
        timeInput.style.cursor = "default";
    } else {
        dateInput.disabled = true;
        timeInput.disabled = true;
        dateInput.style.cursor = "not-allowed";
        timeInput.style.cursor = "not-allowed";
    }
});

async function submit() {

    const btnText = document.getElementById("submit-text");
    const btnIcon = document.getElementById("submit-icon");

    btnText.textContent = "Transferring Funds";
    btnIcon.style.display = "inline-block";

    await new Promise(resolve => setTimeout(resolve, 3000));

    btnText.textContent = "Submit";
    btnIcon.style.display = "none";

    let data;

    if (document.getElementById("transferLaterCheckbox").checked) {
        data = {
            destination: document.getElementById("destination").value,
            amount: document.getElementById("amount").value,
            date: document.getElementById("dateInput").value,
            time: document.getElementById("timeInput").value
        };
    } else {
        data = {
            destination: document.getElementById("destination").value,
            amount: document.getElementById("amount").value
        };
    }

    const response = await fetch(`${window.CONTEXT_PATH}/transaction`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    const message = await response.text();
    if (message === "success") {
        reset();
        await customerDashboardSocket.send(null);
    } else if (message === "redirect") {
        await fetch(`${window.CONTEXT_PATH}/logout`);
        window.location.href = `${window.CONTEXT_PATH}/index.jsp`;
    } else {
        showError(message);
    }
}

function reset() {
    document.getElementById("destination").value = "";
    document.getElementById("amount").value = "";
    document.getElementById("dateInput").value = "";
    document.getElementById("timeInput").value = "";
    document.getElementById("transferLaterCheckbox").checked = false;
}

function showError(msg) {
    const errorMsg = document.getElementById("error-msg");
    errorMsg.innerHTML = "";
    const icon = document.createElement("i");
    icon.className = "fa fa-exclamation-circle";
    icon.style.marginRight = "5px";
    icon.setAttribute("aria-hidden", "true");
    errorMsg.appendChild(icon);
    errorMsg.appendChild(document.createTextNode(msg));
    errorMsg.className = "error-msg";
}

(function () {
    const errorMsg = document.getElementById("error-msg");
    const inputs = document.querySelectorAll(".input-container");
    inputs.forEach((input) => {
        input.addEventListener("click", (e) => {
            errorMsg.innerHTML = "";
        })
    })
})();

window.customerDashboardSocket = new WebSocket("ws://localhost:8080/musashi-banking-system/customerDashboard");

customerDashboardSocket.onopen = () => console.log("WebSocket connected");

customerDashboardSocket.onmessage = async event => {
    console.log(event.data);
    try {
        const data = JSON.parse(event.data);
        loadData(data);
        if (typeof window.renderNotifications === "function") {
            await window.renderNotifications();
        } else {
            throw new Error("window.renderNotifications is not defined");
        }
    } catch (err) {
        console.error("Invalid message format", err);
    }
};

customerDashboardSocket.onclose = () => console.log("WebSocket closed");

const accountElement = document.getElementById("account-num");
mask(accountElement);

// Function to mask the first 4 digits
function maskAccountNumber(number) {
    if (number.length <= 4) return number; // edge case
    const visible = number.slice(-3);
    return "****" + visible;
}

function mask(accountElement){

    // Get full account number from data attribute
    const fullAccountNumber = accountElement.getAttribute("data-full");

    accountElement.textContent = maskAccountNumber(fullAccountNumber);

    accountElement.addEventListener("click", async () => {

        try {
            await navigator.clipboard.writeText(fullAccountNumber);

            // Optional: small tooltip or animation
            accountElement.textContent = "Copied!";
            setTimeout(() => {
                accountElement.textContent = "****" + fullAccountNumber.slice(-3);
            }, 1500);
        } catch (err) {
            console.error("Failed to copy:", err);
        }
    });
}

