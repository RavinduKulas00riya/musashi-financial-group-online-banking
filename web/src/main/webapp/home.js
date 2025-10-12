window.CONTEXT_PATH = document.getElementById('divA').dataset.contextPath;

const notificationsBtn = document.getElementById("notificationsBtn");
const divC = document.getElementById("divC");
const overlay = document.getElementById("overlay");
const divA = document.getElementById("divA");
const divB = document.getElementById("divB");

notificationsBtn.addEventListener("click", () => {
    divC.classList.add("open");
    overlay.classList.add("active");
    divA.style.pointerEvents = "none";
    divB.style.pointerEvents = "none";
    noNewNotification();
});

overlay.addEventListener("click", () => {
    divC.classList.remove("open");
    overlay.classList.remove("active");
    divA.style.pointerEvents = "auto";
    divB.style.pointerEvents = "auto";
    updateNotifications();
    noNewNotification();
});

async function loadDashboard() {
    const panel = document.getElementById("panel");
    panel.style.display = "none";
    panel.innerHTML = "";
    try {
        const response = await fetch(`${window.CONTEXT_PATH}/customer/dashboard.jsp`);
        if (!response.ok) throw new Error("Failed to fetch dashboard.jsp");
        const data = await response.text();

        panel.innerHTML = data;

        const scripts = panel.querySelectorAll("script");
        scripts.forEach(script => {
            if (script.id === "dashboard") {
                const newScript = document.createElement("script");
                newScript.src = script.src;
                // newScript.onload = async () => {
                //     if (typeof window.customerDashboardSocket === "object") {
                //         await window.customerDashboardSocket.send(null);
                //     } else {
                //         throw new Error("window.sendRequest is not defined");
                //     }
                // };
                document.head.appendChild(newScript);
            }
        });

        panel.style.display = "flex";
    } catch (error) {
        console.error("Error:", error.message);
        panel.innerHTML = `<p>Error: ${error.message}</p>`;
    }
}

async function updateNotifications() {
    const response = await fetch(`${window.CONTEXT_PATH}/updateNotifications`);
    const message = await response.text();
    if(message !== "success") {
        alert(message);
        console.log(message);
    }else{
        await renderNotifications();
    }
}

function newNotification() {
    const btn = document.getElementById("notificationsBtn");
    btn.classList.remove("inactive-header");
    const img = document.getElementById("notification-icon");
    const newDiv = document.getElementById("notification-new-div");
    if(img.src.includes("notification.png")){
        img.src = img.src.replace("notification.png", "notification2.png");
        newDiv.style.display = "flex";
    }
}

function noNewNotification() {
    const btn = document.getElementById("notificationsBtn");
    btn.classList.add("inactive-header");
    const img = document.getElementById("notification-icon");
    const newDiv = document.getElementById("notification-new-div");
    if(img.src.includes("notification2.png")){
        img.src = img.src.replace("notification2.png", "notification.png");
        newDiv.style.display = "none";
    }
}

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

    const optionsTime = {hour: "numeric", minute: "2-digit"};

    if (isToday) {
        return dt.toLocaleTimeString([], optionsTime);
    } else if (isYesterday) {
        return "Yesterday " + dt.toLocaleTimeString([], optionsTime);
    } else {
        let optionsDate;
        if (dt.getFullYear() < now.getFullYear()) {
            // include year if it's before current year
            optionsDate = {day: "numeric", month: "long", year: "numeric"};
        } else {
            optionsDate = {day: "numeric", month: "long"};
        }

        return (
            dt.toLocaleDateString([], optionsDate) +
            ", " +
            dt.toLocaleTimeString([], optionsTime)
        );
    }
}

window.renderNotifications = async function () {

    const response = await fetch(`${window.CONTEXT_PATH}/loadNotifications`);
    const json = await response.json();
    const data = json.notifications;

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
    divC.innerHTML = "";

    if(!newContainer.hasChildNodes() && !oldContainer.hasChildNodes()){
        noNotification();
        return;
    }

    if (newContainer.hasChildNodes()) {
        divC.appendChild(newHeading);
        divC.appendChild(newContainer);
        newNotification();
    }

    if (oldContainer.hasChildNodes()) {
        divC.appendChild(oldHeading);
        divC.appendChild(oldContainer);
    }
}

function noNotification() {
    const divC = document.getElementById("divC");
    divC.style.justifyContent = "center";

    const img = document.createElement("img");
    img.src = `${window.CONTEXT_PATH}/images/no-results.png`;
    img.width = 100;
    img.height = 100;
    img.alt = "";
    img.style.filter = "invert(0.45)";
    img.id = "no-results-img";

    const span = document.createElement("span");
    span.id = "no-notifications-span";
    span.style.filter = "invert(0.45)";
    span.style.fontFamily = "bold";
    span.style.textAlign = "center";
    span.textContent = "No Notifications Yet";
    span.style.width = 255;

    divC.appendChild(img);
    divC.appendChild(span);
}