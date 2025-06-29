document.getElementById("login-form").addEventListener("submit", async function (e) {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (response.ok) {
            localStorage.setItem("username", data.username);

            window.location.href = "/home.html";
        } else {
            alert(data.message || "Đăng nhập thất bại");
        }
    } catch (error) {
        console.error("Lỗi:", error);
    }
});
