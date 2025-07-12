window.onload = function() {
    loadAccounts();
}

async function loadAccounts() {

    const accountNumber = document.querySelector('input[name="accountNo"]').value;
    const name = document.querySelector('input[name="name"]').value;

    const data = {
        accountNumber: accountNumber,
        name: name,
    }

    const response = await fetch('http://localhost:8080/musashi-banking-system/loadCustomers',{
        method: 'POST',
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    });

    const json = await response.json();
    console.log(json);
    fillTable(json);
}

function fillTable(data) {
    const tbody = document.querySelector("#customerTable tbody");
    tbody.innerHTML = "";

    data.forEach(record => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${record.accountNo}</td>
            <td>${record.name}</td>
            <td>${record.mobile}</td>
            <td>${record.email}</td>
            <td>${parseFloat(record.balance).toLocaleString()}</td>
            <td>${record.status}</td>
            <td></td>
        `;

        // Create the cancel button
        const actionButton = document.createElement('button');
        if (record.active) {
            actionButton.textContent = 'Deactivate';
        }else{
            actionButton.textContent = 'Activate';
        }
        actionButton.onclick = function () {
            changeAccountStatus(record.accountNo); // Use a unique identifier like transactionId
        };

        // Append button to the last td
        row.lastElementChild.appendChild(actionButton);

        tbody.appendChild(row);
    });
}

async function changeAccountStatus(id) {
    const response = await fetch('http://localhost:8080/musashi-banking-system/changeAccountStatus?id=' + id);

    const text = await response.text();

    if(text === 'success') {
        loadAccounts();
    }else{
        alert("Please try again later.");
    }
}