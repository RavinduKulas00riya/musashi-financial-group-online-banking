async function searchHistory() {
    const accountNumber = document.querySelector('input[name="accountNumber"]').value;
    const name = document.querySelector('input[name="name"]').value;
    const status = document.querySelector('select[name="status"]').value;
    const sentOrReceived = document.querySelector('select[name="sent_or_received"]').value;

    const data = {
        accountNumber: accountNumber,
        name: name,
        status: status,
        sentOrReceived:sentOrReceived
    };

    const response = await fetch('http://localhost:8080/musashi-banking-system/loadHistory', {
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

function fillTable(data) {
    const tbody = document.querySelector('table tbody') || document.createElement('tbody');
    tbody.innerHTML = '';

    data.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.accountNumber}</td>
            <td>${item.name}</td>
            <td>${item.transactionType}</td>
            <td>${item.amount}</td>
            <td>${item.dateTime}</td>
            <td>${item.status}</td>
        `;
        tbody.appendChild(row);
    });

    if (!document.querySelector('table tbody')) {
        document.querySelector('table').appendChild(tbody);
    }
}