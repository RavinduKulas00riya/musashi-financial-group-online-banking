const images = document.querySelectorAll('.bg-image');
let currentImage = 0;

function changeBackground() {
    const nextImage = (currentImage + 1) % images.length;

    images[nextImage].classList.add('active');

    setTimeout(() => {
        images[currentImage].classList.remove('active');
        currentImage = nextImage;
    }, 1000);
}
setInterval(changeBackground, 5000);

async function login() {

    const email = document.querySelector('input[name="email"]').value;
    const password = document.querySelector('input[name="password"]').value;

    const data = {
        email: email,
        password: password,
    }

    const response = await fetch('http://localhost:8080/musashi-banking-system/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    const text = await response.text();

    if(text === "success") {
        window.location.href="./customer/home.jsp";
    }else{
        showError(text);
    }

}

function showPasswordDiv() {

    const detailsDiv = document.getElementsByClassName("details-div")[0];
    const passwordsDiv = document.getElementsByClassName("passwords-div")[0];

    if (detailsDiv.style.display === 'none') {
        detailsDiv.style.display = 'block';
        passwordsDiv.style.display = 'none';
    } else {
        detailsDiv.style.display = 'none';
        passwordsDiv.style.display = 'block';
    }


}

function hideError() {
    const errorMessage = document.getElementById('error-msg');
    errorMessage.innerHTML = '';
}

function showError(message) {
    const errorMessage = document.getElementById('error-msg');
    errorMessage.innerHTML = `<i class=\"bi bi-exclamation-circle\"></i>`+" "+ message;
}

async function register(param) {

    let data;

    if(param === 'details'){
        const name = document.querySelector('input[name="name"]').value;
        const email = document.querySelector('input[name="email"]').value;
        const mobile = document.querySelector('input[name="mobile"]').value;
        const amount = document.querySelector('input[name="amount"]').value;

        data = {
            name: name,
            email: email,
            mobile: mobile,
            amount: amount,
        }
    }else{

        const password = document.querySelector('input[name="password"]').value;
        const confirmPassword = document.querySelector('input[name="confirm-password"]').value;

        data = {
            password: password,
            confirmPassword: confirmPassword,
        }

    }

    const response = await fetch('http://localhost:8080/musashi-banking-system/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    const text = await response.text();

    if(text === "success") {
        showPasswordDiv();
    }else if(text === "done") {
        window.location.href="./index.jsp";
    }else{
        showError(text);
    }

}