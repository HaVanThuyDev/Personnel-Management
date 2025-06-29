document.addEventListener("DOMContentLoaded", function () {
    fetch("http://localhost:8080/api/statistics/summary")
        .then(response => {
            if (!response.ok) {
                throw new Error("Không thể lấy dữ liệu thống kê!");
            }
            return response.json();
        })
        .then(data => {
            const labels = [
                "Nhân viên",
                "Phòng ban",
                "Người dùng",
                "Hợp đồng"
            ];

            const values = [
                parseInt(data.totalEmployees),
                parseInt(data.totalDepartments),
                parseInt(data.totalUsers),
                parseInt(data.totalContracts)
            ];

            renderPieChart(labels, values);
            renderBarChart(labels, values);
            renderLineChart(labels, values);
        })
        .catch(error => {
            console.error("Lỗi:", error.message);
        });
});

function renderPieChart(labels, data) {
    new Chart(document.getElementById("pieChart"), {
        type: "pie",
        data: {
            labels: labels,
            datasets: [{
                label: "Thống kê",
                data: data,
                backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0"]
            }]
        }
    });
}

function renderBarChart(labels, data) {
    new Chart(document.getElementById("barChart"), {
        type: "bar",
        data: {
            labels: labels,
            datasets: [{
                label: "Số lượng",
                data: data,
                backgroundColor: "#36A2EB"
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false }
            }
        }
    });
}

function renderLineChart(labels, data) {
    new Chart(document.getElementById("lineChart"), {
        type: "line",
        data: {
            labels: labels,
            datasets: [{
                label: "Số lượng",
                data: data,
                borderColor: "#FF6384",
                fill: false,
                tension: 0.3
            }]
        },
        options: {
            responsive: true
        }
    });
}
