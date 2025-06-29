document.addEventListener('DOMContentLoaded', function () {
    const apiUrl = 'http://localhost:8080/api/payroll';
    const payrollTableBody = document.getElementById('payrollData');
    const payrollForm = document.getElementById('payrollForm');
    const btnAddNew = document.getElementById('btnAddNew');
    const btnSave = document.getElementById('saveBtn');
    const btnSearch = document.getElementById('searchBtn');
    const btnExport = document.getElementById('exportBtn');
    const payrollIdInput = document.getElementById('payrollId');
    const employeeIdInput = document.getElementById('employeeId');
    const fullNameInput = document.getElementById('fullName');
    const monthInput = document.getElementById('month');
    const yearInput = document.getElementById('year');
    const basicSalaryInput = document.getElementById('basicSalary');
    const overtimePayInput = document.getElementById('overtimePay');
    const finalSalaryInput = document.getElementById('finalSalary');
    const searchInput = document.getElementById('searchInput');
    payrollForm.style.display = 'none';

    function fetchPayrolls() {
        fetch(`${apiUrl}/list`)
            .then(res => res.json())
            .then(data => {
                renderTable(data);
            })
            .catch(err => console.error('Lỗi khi lấy dữ liệu:', err));
    }
    function renderTable(data) {
        payrollTableBody.innerHTML = '';
        data.forEach(payroll => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${payroll.payrollId}</td>
                <td>${payroll.employeeId}</td>
                <td>${payroll.fullName}</td>
                <td>${payroll.month}</td>
                <td>${payroll.year}</td>
                <td>${payroll.basicSalary}</td>
                <td>${payroll.overtimePay}</td>
                <td>${payroll.finalSalary}</td>
                <td>
                    <button onclick="editPayroll(${payroll.payrollId})"> Sửa </button>
                    <button onclick="deletePayroll(${payroll.payrollId})"> Xóa </button>
                </td>
            `;
            payrollTableBody.appendChild(row);
        });
    }
    function clearForm() {
        payrollIdInput.value = '';
        employeeIdInput.value = '';
        fullNameInput.value = '';
        monthInput.value = '';
        yearInput.value = '';
        basicSalaryInput.value = '';
        overtimePayInput.value = '';
        finalSalaryInput.value = '';
    }
    function calculateFinalSalary() {
        const basicSalary = parseFloat(basicSalaryInput.value) || 0;
        const overtimePay = parseFloat(overtimePayInput.value) || 0;
        const finalSalary = basicSalary + overtimePay;
        finalSalaryInput.value = finalSalary.toFixed(2); // Hiển thị 2 chữ số thập phân
    }
    basicSalaryInput.addEventListener('input', calculateFinalSalary);
    overtimePayInput.addEventListener('input', calculateFinalSalary);

    btnAddNew.addEventListener('click', () => {
        clearForm();
        payrollForm.style.display = 'block';
        calculateFinalSalary();
    });
    btnSave.addEventListener('click', () => {
        const basicSalary = parseFloat(basicSalaryInput.value) || 0;
        const overtimePay = parseFloat(overtimePayInput.value) || 0;
        const finalSalary = basicSalary + overtimePay; // Tính finalSalary

        const payroll = {
            employeeId: employeeIdInput.value,
            fullName: fullNameInput.value,
            month: monthInput.value,
            year: yearInput.value,
            basicSalary: basicSalary,
            overtimePay: overtimePay,
            finalSalary: finalSalary
        };
        if (!payroll.employeeId || !payroll.fullName || !payroll.month || !payroll.year) {
            alert('Vui lòng điền đầy đủ thông tin!');
            return;
        }

        const id = payrollIdInput.value;
        const method = id ? 'PUT' : 'POST';
        const url = id ? `${apiUrl}/update/${id}` : `${apiUrl}/add`;

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payroll)
        })
            .then(res => {
                if (!res.ok) throw new Error(`Lỗi khi lưu: ${res.statusText}`);
                return res.json();
            })
            .then(() => {
                fetchPayrolls();
                payrollForm.style.display = 'none';
                clearForm();
                alert('Lưu thành công!');
            })
            .catch(err => {
                console.error('Lỗi khi lưu:', err);
                alert(`Lỗi: ${err.message}`);
            });
    });

    window.editPayroll = function (id) {
        fetch(`${apiUrl}/list`)
            .then(res => res.json())
            .then(data => {
                const payroll = data.find(p => p.payrollId === id);
                if (payroll) {
                    payrollIdInput.value = payroll.payrollId;
                    employeeIdInput.value = payroll.employeeId;
                    fullNameInput.value = payroll.fullName;
                    monthInput.value = payroll.month;
                    yearInput.value = payroll.year;
                    basicSalaryInput.value = payroll.basicSalary;
                    overtimePayInput.value = payroll.overtimePay;
                    finalSalaryInput.value = payroll.finalSalary;
                    payrollForm.style.display = 'block';
                    calculateFinalSalary();
                }
            })
            .catch(err => console.error('Lỗi khi lấy dữ liệu chỉnh sửa:', err));
    };

    window.deletePayroll = function (id) {
        if (confirm('Bạn có chắc chắn muốn xóa không?')) {
            fetch(`${apiUrl}/delete/${id}`, {
                method: 'DELETE'
            })
                .then(res => {
                    if (!res.ok) throw new Error('Lỗi khi xóa');
                    fetchPayrolls();
                    alert('Xóa thành công!');
                })
                .catch(err => {
                    console.error('Lỗi khi xóa:', err);
                    alert(`Lỗi: ${err.message}`);
                });
        }
    };
    btnSearch.addEventListener('click', () => {
        const keyword = searchInput.value.trim();
        if (keyword) {
            fetch(`${apiUrl}/search?keyword=${encodeURIComponent(keyword)}`)
                .then(res => {
                    if (!res.ok) throw new Error('Lỗi tìm kiếm');
                    return res.json();
                })
                .then(data => renderTable(data))
                .catch(err => {
                    console.error('Lỗi tìm kiếm:', err);
                    alert(`Lỗi: ${err.message}`);
                });
        } else {
            fetchPayrolls();
        }
    });
    btnExport.addEventListener('click', () => {
        fetch(`${apiUrl}/export`)
            .then(res => {
                if (!res.ok) throw new Error('Lỗi xuất Excel');
                return res.blob();
            })
            .then(blob => {
                const downloadUrl = URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = downloadUrl;
                link.download = 'payrolls.xlsx';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
                URL.revokeObjectURL(downloadUrl);
                alert('Xuất Excel thành công!');
            })
            .catch(err => {
                console.error('Lỗi xuất Excel:', err);
                alert(`Lỗi: ${err.message}`);
            });
    });

    fetchPayrolls();
});