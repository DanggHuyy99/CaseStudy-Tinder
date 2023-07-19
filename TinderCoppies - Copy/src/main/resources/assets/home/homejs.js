document.getElementById("swiperight").style.display = 'none';
document.getElementById("home").style.display = 'none';
window.onload = () => {
    // document.getElementById("swiperight").style.display = 'none';
    document.getElementById("logo").style.height = '130px';
    document.getElementById("logo").style.width = '130px';
    setTimeout(() => {
        // document.getElementById("swiperight").style.display = 'none';
        document.getElementById("logo").style.height = '100px';
        document.getElementById("logo").style.width = '100px';
    }, 500)
    setTimeout(() => {
        document.getElementById("loadding").style.display = 'none';
    }, 1000)
    setTimeout(() => {
        document.getElementById("swiperight").style.display = 'block';
        document.getElementById("home").style.display = 'block';
    }, 1100)
}

const loginButton = document.querySelector('.btn-login');
const loginForm = document.getElementById('login-form');

// Xử lý sự kiện click trên nút "Log in"
loginButton.addEventListener('click', function () {
    loginForm.style.display = 'block';
});

// Xử lý sự kiện submit form
loginForm.querySelector('form').addEventListener('submit', function (event) {
    event.preventDefault();
    console.log('Submitted');
    loginForm.style.display = 'none';
});

document.addEventListener("DOMContentLoaded", function () {
    var loginForm = document.getElementById("login-form");
    var closeBtn = document.getElementById("close-btn");

    function toggleLoginForm() {
        loginForm.style.display = loginForm.style.display === "none" ? "block" : "none";
    }

    closeBtn.addEventListener("click", toggleLoginForm);

    window.addEventListener("click", function (event) {
        if (event.target === loginForm) {
            toggleLoginForm();
        }
    });
});

