window.onload = function() {
    loadHistory();
}

async function loadHistory() {

    const accountNumber = document.querySelector('input[name="accountNo"]').value;
    const status = document.querySelector('select[name="status"]').value;

    const data = {
        accountNumber: accountNumber,
        status: status,
    }

    const response = await fetch('http://localhost:8080/musashi-banking-system/loadAllHistory', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    const contentType = response.headers.get("Content-Type");

    if (contentType.includes("application/json")) {
        const result = await response.json();
        fillTable(result);

    } else if (contentType.includes("text/plain")) {
        const message = await response.text();
        alert(message);
    }
}

function fillTable(data){
    const tbody = document.querySelector("#historyTable tbody");
    tbody.innerHTML = "";

    data.forEach(record => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${record.from}</td>
            <td>${record.to}</td>
            <td>${parseFloat(record.amount).toLocaleString()}</td>
            <td>${record.dateTime}</td>
            <td>${record.status}</td>
        `;
        tbody.appendChild(row);
    });
}