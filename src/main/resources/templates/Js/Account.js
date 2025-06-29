const apiUrl = "http://localhost:8080/api/account/all";

document.addEventListener("DOMContentLoaded", () => {
    const tbody = document.querySelector("#userTable tbody");
    const searchInput = document.getElementById("searchInput");
    const editForm = document.getElementById("editUserForm");
    const editContainer = document.getElementById("editFormContainer");

    let allUsers = [];
    async function loadUsers() {
        try {
            const res = await fetch(apiUrl);
            if (!res.ok) throw new Error("Lỗi tải dữ liệu.");
            allUsers = await res.json();
            displayUsers(allUsers);
        } catch (e) {
            alert("Lỗi khi tải dữ liệu người dùng!");
            console.error(e);
        }
    }
    function displayUsers(users) {
        const filter = searchInput.value.toLowerCase();
        tbody.innerHTML = "";

        users
            .filter(u =>
                u.email.toLowerCase().includes(filter) ||
                u.username.toLowerCase().includes(filter)
            )
            .forEach(user => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.email}</td>
                    <td>${user.username}</td>
                    <td>..........</td>
                    <td>${user.role}</td>
                    <td>${user.accountStatus || "Không rõ"}</td>
                    <td>
                        <button class="edit-btn">Sửa</button>
                        <button class="delete-btn">Xóa</button>
                    </td>
                `;
                row.querySelector(".edit-btn").addEventListener("click", () => openEditForm(user));
                row.querySelector(".delete-btn").addEventListener("click", async () => {
                    if (confirm("Bạn có chắc muốn xóa người dùng này không?")) {
                        try {
                            const response = await fetch(`http://localhost:8080/api/account/delete/${user.id}`, {
                                method: "DELETE"
                            });

                            if (response.ok) {
                                alert("Đã xóa!");
                                await loadUsers();
                            } else {
                                const result = await response.json();
                                alert(result.message || "Không thể xóa người dùng.");
                            }
                        } catch (err) {
                            alert("Lỗi khi xóa người dùng!");
                            console.error(err);
                        }
                    }
                });

                tbody.appendChild(row);
            });
    }

    function openEditForm(user) {
        document.getElementById("editId").value = user.id;
        document.getElementById("editEmail").value = user.email;
        document.getElementById("editUsername").value = user.username;
        document.getElementById("editRole").value = user.role;
        document.getElementById("editStatus").value = user.accountStatus || "INACTIVE";
        editContainer.classList.remove("hidden");
        editContainer.classList.add("show"); // Thêm class show để hiển thị modal
    }
    window.closeForm = function () {
        editContainer.classList.remove("show");
        editContainer.classList.add("hidden");
    };
    window.goBack = function () {
        window.location.href = "AdminHome.html";
    };
    editForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const id = document.getElementById("editId").value;
        const updatedUser = {
            email: document.getElementById("editEmail").value,
            username: document.getElementById("editUsername").value,
            password: document.getElementById("editPassword").value,
            role: document.getElementById("editRole").value,
            accountStatus: document.getElementById("editStatus").value
        };

        try {
            const response = await fetch(`http://localhost:8080/api/account/update/${id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(updatedUser)
            });

            if (response.ok) {
                alert("Cập nhật thành công!");
                closeForm();
                await loadUsers();
            } else {
                const result = await response.json();
                alert(result.message || "Lỗi khi cập nhật!");
            }
        } catch (err) {
            alert("Lỗi khi cập nhật!");
            console.error(err);
        }
    });
    searchInput.addEventListener("input", () => {
        displayUsers(allUsers);
    });
    loadUsers();
});