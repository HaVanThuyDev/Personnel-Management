document.addEventListener("DOMContentLoaded", () => {
    fetch('http://localhost:8080/api/statistics/summary')
        .then(response => response.json())
        .then(data => {
            const payrollAmount = Number(data.totalPayrollAmount);

            if (isNaN(payrollAmount)) {
                console.error("Dữ liệu lương không hợp lệ");
                return;
            }

            const ctx = document.getElementById('payrollChart').getContext('2d');

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: ['Tổng Lương'],
                    datasets: [{
                        label: 'Tổng Lương (VND)',
                        data: [payrollAmount],
                        backgroundColor: 'rgba(75, 192, 192, 0.5)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: value => value.toLocaleString('vi-VN')
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            display: true
                        }
                    }
                }
            });
        })
        .catch(error => {
            console.error("Lỗi khi gọi API:", error);
        });
    document.getElementById('exportExcelBtn').addEventListener('click', function() {
        // Giả sử payrollLabels và payrollData đã có sẵn ở scope ngoài
        const wsData = [['Tháng', 'Tổng lương (VNĐ)']];
        for (let i = 0; i < payrollLabels.length; i++) {
            wsData.push([payrollLabels[i], payrollData[i]]);
        }
        const ws = XLSX.utils.aoa_to_sheet(wsData);
        const wb = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, "TongLuong");
        XLSX.writeFile(wb, "TongLuong.xlsx");
    });
    document.getElementById('backBtn').addEventListener('click', function() {
        window.history.back();
    });
});
