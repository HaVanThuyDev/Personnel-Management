
    document.addEventListener("DOMContentLoaded", () => {
    const sendOtpBtn = document.getElementById("sendOtpButton");
    const registerBtn = document.getElementById("registerButton");
    const otpInput = document.getElementById("otpInput");

    const usernameInput = document.querySelector('input[name="username"]');
    const emailInput = document.querySelector('input[name="email"]');
    const passwordInput = document.querySelector('input[name="password"]');
    sendOtpBtn.addEventListener("click", async () => {
    const username = usernameInput.value.trim();
    const email = emailInput.value.trim();
    const password = passwordInput.value.trim();

    if (!username || !email || !password) {
    alert("Vui lòng nhập đầy đủ Họ tên, Email và Mật khẩu trước khi gửi OTP!");
    return;
}

    try {
    const response = await fetch("http://localhost:8080/api/auth/register", {
    method: "POST",
    headers: {
    "Content-Type": "application/json"
},
    body: JSON.stringify({
    username,
    email,
    password,
    role: "EMPLOYEE"
})
});

    const result = await response.json();
    if (response.ok) {
    alert(result.message);
    otpInput.disabled = false;
    registerBtn.disabled = false;
} else {
    alert("Không thể gửi OTP: " + result.message);
}
} catch (err) {
    alert("Gửi OTP thất bại!");
    console.error(err);
}
});
    registerBtn.addEventListener("click", async () => {
    const otp = otpInput.value.trim();
    const email = emailInput.value.trim();

    if (!otp || !email) {
    alert("Vui lòng nhập mã OTP!");
    return;
}

    try {
    const response = await fetch("http://localhost:8080/api/auth/verify-otp", {
    method: "POST",
    headers: {
    "Content-Type": "application/json"
},
    body: JSON.stringify({ email, otp })
});

    const result = await response.json();
    if (response.ok) {
    alert("" + result.message);
} else {
    alert("" + result.message);
}
} catch (err) {
    alert(" Đăng ký thất bại!");
    console.error(err);
}
});
});

