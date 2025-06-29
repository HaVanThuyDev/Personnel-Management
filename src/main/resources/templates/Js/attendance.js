document.addEventListener("DOMContentLoaded", async () => {
    const userData = JSON.parse(localStorage.getItem("loggedInUser"));

    if (!userData) {
        alert("Bạn chưa đăng nhập!");
        window.location.href = "login.html";
        return;
    }

    const username = userData.username;
    let userId = null;

    try {
        const res = await fetch(`http://localhost:8080/api/account/search?keyword=${username}`);
        const users = await res.json();
        const currentUser = users.find(u => u.username === username);

        if (!currentUser || !currentUser.id) {
            throw new Error("Không tìm thấy thông tin người dùng!");
        }

        userId = currentUser.id;
        localStorage.setItem("userId", userId);
        document.getElementById("selectedName").textContent = currentUser.username;

        loadWorkingDays();
    } catch (error) {
        alert("Lỗi khi tải thông tin người dùng: " + error.message);
        console.error(error);
        return;
    }

    window.logout = function () {
        localStorage.removeItem("loggedInUser");
        localStorage.removeItem("userId");
        window.location.href = "login.html";
    };

    window.checkIn = async function () {
        const userId = localStorage.getItem("userId");

        if (!userId) {
            alert("Không tìm thấy người dùng! Vui lòng đăng nhập lại.");
            return;
        }
        const localCheckIn = new Date();
        const formattedCheckIn = localCheckIn.toLocaleTimeString("vi-VN", {
            hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false
        });
        document.getElementById("result").textContent = `✅ Check-in lúc: ${formattedCheckIn}`;
        document.getElementById("startTime").textContent = formattedCheckIn;

        try {
            const res = await fetch(`http://localhost:8080/api/attendance/check-in/${userId}`, {
                method: "POST"
            });

            const result = await res.json();
            console.log("Check-in response:", result);

            if (!res.ok) {
                throw new Error(result.message || "Không thể check-in!");
            }
            if (result.checkIn) {
                const serverCheckIn = formatVietnamTime(result.checkIn);
                document.getElementById("result").textContent = `✅ Check-in lúc: ${serverCheckIn}`;
                document.getElementById("startTime").textContent = serverCheckIn;
            }

            loadWorkingDays();
        } catch (err) {
            alert("Không thể check-in! " + err.message);
            console.error(err);
        }
    };

    window.checkOut = async function () {
        const userId = localStorage.getItem("userId");

        if (!userId) {
            alert("Không tìm thấy người dùng! Vui lòng đăng nhập lại.");
            return;
        }
        const localCheckOut = new Date();
        const formattedCheckOut = localCheckOut.toLocaleTimeString("vi-VN", {
            hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false
        });
        document.getElementById("result").textContent += `\n🕒 Check-out lúc: ${formattedCheckOut}`;
        document.getElementById("endTime").textContent = formattedCheckOut;

        try {
            const res = await fetch(`http://localhost:8080/api/attendance/check-out/${userId}`, {
                method: "POST"
            });

            const result = await res.json();
            console.log("Check-out response:", result);

            if (!res.ok) {
                throw new Error(result.message || "Không thể check-out!");
            }
            if (result.checkIn && result.checkOut) {
                const checkInTime = new Date(result.checkIn);
                const checkOutTime = new Date(result.checkOut);

                const formattedCheckIn = formatVietnamTime(result.checkIn);
                const formattedCheckOutServer = formatVietnamTime(result.checkOut);

                document.getElementById("result").textContent += `\n🕒 Check-out (server): ${formattedCheckOutServer}`;
                document.getElementById("endTime").textContent = formattedCheckOutServer;
                document.getElementById("startTime").textContent = formattedCheckIn;

                if (!isNaN(checkInTime) && !isNaN(checkOutTime)) {
                    const totalMs = checkOutTime - checkInTime;
                    const hours = Math.floor(totalMs / (1000 * 60 * 60));
                    const minutes = Math.floor((totalMs % (1000 * 60 * 60)) / (1000 * 60));
                    document.getElementById("totalTime").textContent = `${hours} giờ ${minutes} phút`;
                } else {
                    document.getElementById("totalTime").textContent = "Không xác định";
                }
            }

            loadWorkingDays();
        } catch (err) {
            alert("Không thể check-out! " + err.message);
            console.error(err);
        }
    };

    async function loadWorkingDays() {
        const userId = localStorage.getItem("userId");

        if (!userId) {
            document.getElementById("workingDays").textContent = "Không xác định";
            return;
        }

        try {
            const res = await fetch(`http://localhost:8080/api/attendance/working-days/${userId}`);
            const workingDays = await res.text();
            document.getElementById("workingDays").textContent = workingDays;
        } catch (err) {
            console.error("Lỗi khi tải số ngày đi làm:", err);
            document.getElementById("workingDays").textContent = "Không thể tải";
        }
    }

    function formatVietnamTime(dateStr) {
        if (!dateStr) return "Không có";

        const utcDate = new Date(dateStr);
        if (isNaN(utcDate.getTime())) return "Không hợp lệ";

        const vnOffsetMs = 7 * 60 * 60 * 1000; // UTC+7
        const vnDate = new Date(utcDate.getTime() + vnOffsetMs);

        return vnDate.toLocaleTimeString("vi-VN", {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false
        });
    }
});