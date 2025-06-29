document.addEventListener("DOMContentLoaded", async () => {
    const userData = JSON.parse(localStorage.getItem("loggedInUser"));

    if (!userData) {
        alert("Bạn chưa đăng nhập!");
        window.location.href = "login.html";
        return;
    }

    window.logout = function () {
        localStorage.removeItem("loggedInUser");
        window.location.href = "login.html";
    };

    let currentUser;
    try {
        const response = await fetch(`http://localhost:8080/api/account/search?keyword=${userData.username}`);
        if (!response.ok) {
            throw new Error(`Lỗi HTTP: ${response.status} ${response.statusText}`);
        }
        const users = await response.json();

        currentUser = users.find(u => u.username === userData.username);
        if (!currentUser) {
            alert("Không tìm thấy tài khoản!");
            return;
        }

        if (!currentUser.id) {
            console.error("Không tìm thấy id trong dữ liệu người dùng:", currentUser);
            alert("Lỗi: Dữ liệu người dùng không đầy đủ.");
            return;
        }

        const tableBody = document.getElementById("userTableBody");
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${currentUser.email}</td>
            <td>${currentUser.username}</td>
            <td>${currentUser.password}</td>
            <td>
                <button class="btn-edit">Sửa</button>
            </td>
        `;

        tableBody.appendChild(row);

        row.querySelector(".btn-edit").addEventListener("click", function () {
            editCurrentUser(currentUser);
        });

    } catch (error) {
        console.error("Lỗi khi tải thông tin tài khoản:", error);
        alert(`Không thể tải thông tin: ${error.message}`);
    }

    const form = document.getElementById("accountForm");
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const email = document.getElementById("email").value;
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        if (!email || !username) {
            alert("Vui lòng điền email và username!");
            return;
        }

        const updatedData = { email, username, password };

        try {
            const userId = currentUser.id;
            if (!userId) {
                alert("Lỗi: Không tìm thấy ID người dùng.");
                return;
            }

            const res = await fetch(`http://localhost:8080/api/account/update/${userId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(updatedData)
            });

            if (res.ok) {
                alert("✅ Cập nhật thành công!");
                localStorage.setItem("loggedInUser", JSON.stringify({ ...userData, email, username }));
                location.reload();
            } else {
                const msg = await res.text();
                alert(`Lỗi cập nhật: ${msg}`);
            }

        } catch (error) {
            console.error("Lỗi khi cập nhật:", error);
            alert(`Không thể cập nhật: ${error.message}`);
        }
    });
});

function editCurrentUser(user) {
    document.getElementById("email").value = user.email;
    document.getElementById("username").value = user.username;
    document.getElementById("password").value = user.password || "";

    document.getElementById("accountForm").style.display = "block";
    document.getElementById("accountForm").scrollIntoView({ behavior: "smooth" });
}