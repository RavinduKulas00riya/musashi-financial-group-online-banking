async function loadDashboard() {
    const response = await fetch('http://localhost:8080/musashi-banking-system/loadDashboard');

    const json = await response.json();

    updateStatistics(json.statistics);
    fillTable(json.dailyBalances);
}

function updateStatistics(stats) {
    document.getElementById("totalCustomers").textContent = stats.totalCustomers;
    document.getElementById("activeCustomers").textContent = stats.activeCustomers;
    document.getElementById("totalBalance").textContent = parseFloat(stats.totalBalance).toLocaleString();
}

function fillTable(records) {
    const tbody = document.querySelector("#balanceTable tbody");
    tbody.innerHTML = "";

    records.forEach(record => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${record.date}</td>
            <td>${parseFloat(record.balance).toLocaleString()}</td>
            <td>${record.change}</td>
        `;
        tbody.appendChild(row);
    });
}