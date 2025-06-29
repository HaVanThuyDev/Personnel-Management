document.addEventListener("DOMContentLoaded", () => {
    const loginButton = document.getElementById('loginButton');

    if (loginButton) {
        loginButton.addEventListener('click', async () => {
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value.trim();

            if (!email || !password) {
                alert("Vui lòng nhập đầy đủ email và mật khẩu!");
                return;
            }

            try {
                const response = await fetch('http://localhost:8080/api/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email, password })
                });

                const result = await response.json();

                if (response.ok) {
                    if (result.accountStatus !== "ACTIVE") {
                        alert("Tài khoản của bạn chưa được kích hoạt!");
                        return;
                    }
                    localStorage.setItem("loggedInUser", JSON.stringify(result));
                    alert(result.message);

                    switch (result.role) {
                        case "ADMIN":
                            window.location.href = "AdminHome.html";
                            break;
                        case "EMPLOYEE":
                            window.location.href = "EmployeeHome.html";
                            break;
                        case "MANAGER":
                            window.location.href = "ManagerHome.html";
                            break;
                        case "ACCOUNTANT":
                            window.location.href = "AccountantHome.html";
                            break;
                        case "HR":
                            window.location.href = "HRHome.html";
                            break;
                        default:
                            alert("Không xác định được quyền truy cập!");
                    }

                } else {
                    alert(result.message || "Đăng nhập thất bại!");
                }

            } catch (error) {
                alert("Không thể kết nối đến máy chủ!");
                console.error("Lỗi login:", error);
            }
        });
    }
});
