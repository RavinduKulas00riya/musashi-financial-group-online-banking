let transactionStatus = "";

async function searchHistory(status) {
    transactionStatus = status;
    const accountNumber = document.querySelector('input[name="accountNumber"]').value;
    const name = document.querySelector('input[name="name"]').value;

    let data = null;

    if (status === 'COMPLETED') {
        const sentOrReceived = document.querySelector('select[name="sent_or_received"]').value;

        data = {
            accountNumber: accountNumber,
            name: name,
            status: status,
            sentOrReceived:sentOrReceived
        };
    }else{

        data = {
            accountNumber: accountNumber,
            name: name,
            status: status
        };
    }

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

    if(transactionStatus === 'COMPLETED') {

        data.forEach(item => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td>${item.accountNumber}</td>
            <td>${item.name}</td>
            <td>${item.transactionType}</td>
            <td>${item.amount}</td>
            <td>${item.dateTime}</td>
        `;
            tbody.appendChild(row);
        });

    }else{

        data.forEach(item => {
            const row = document.createElement('tr');
            row.innerHTML = `
        <td>${item.accountNumber}</td>
        <td>${item.name}</td>
        <td>${item.transactionType}</td>
        <td>${item.amount}</td>
        <td>${item.dateTime}</td>
        <td></td> <!-- Placeholder for the button -->
    `;

            // Create the cancel button
            const cancelButton = document.createElement('button');
            cancelButton.textContent = 'Cancel';
            cancelButton.onclick = function () {
                cancelTransaction(item.transactionId); // Use a unique identifier like transactionId
            };

            // Append button to the last td
            row.lastElementChild.appendChild(cancelButton);

            // Append the row to the table body
            tbody.appendChild(row);
        });

    }

    if (!document.querySelector('table tbody')) {
        document.querySelector('table').appendChild(tbody);
    }
}