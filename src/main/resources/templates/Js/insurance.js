const API_BASE = "http://localhost:8080";
const API_INSURANCE = `${API_BASE}/api/insurances`;
const API_EMPLOYEE_DETAIL = `${API_BASE}/employees/api/detail`;

document.addEventListener("DOMContentLoaded", () => {
    loadInsurances();

    document.getElementById("insuranceForm").addEventListener("submit", handleFormSubmit);
    document.getElementById("employeeId").addEventListener("input", debounce(fetchEmployeeDetails, 300));
    document.getElementById("btnExport").addEventListener("click", exportToExcel);
    document.getElementById("btnAddNew").addEventListener("click", () => {
        resetForm();
        showForm(true);
    });
    document.getElementById("cancelButton").addEventListener("click", () => {
        resetForm();
        showForm(false);
    });
});

function loadInsurances() {
    fetch(API_INSURANCE)
        .then(res => {
            if (!res.ok) throw new Error(`Lỗi API: ${res.status}`);
            return res.json();
        })
        .then(data => {
            console.log("Dữ liệu bảo hiểm:", data);
            renderInsurances(data);
        })
        .catch(err => {
            console.error("Không thể tải danh sách bảo hiểm:", err);
            alert("Không thể tải danh sách bảo hiểm từ máy chủ.\nChi tiết: " + err.message);
        });
}
function renderInsurances(data) {
    const tbody = document.getElementById("insuranceTableBody");
    tbody.innerHTML = "";

    data.forEach(insurance => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${insurance.insuranceId || "undefined"}</td>  <!-- Sửa thành insurance.insuranceId -->
            <td>${insurance.employeeName || "Không xác định"}</td>  <!-- Sửa thành insurance.employeeName -->
            <td>${insurance.departmentName || "Không xác định"}</td>  <!-- Sửa thành insurance.departmentName -->
            <td>${insurance.insuranceType}</td>
            <td>${insurance.insuranceAmount}</td>
            <td>${insurance.startDate || "null"}</td>
            <td>${insurance.endDate || "null"}</td>
            <td>
                <button class="edit" onclick="editInsurance(${insurance.insuranceId})">✏️ Sửa</button>  <!-- Sửa thành insurance.insuranceId -->
                <button class="delete" onclick="deleteInsurance(${insurance.insuranceId})">🗑️ Xoá</button>  <!-- Sửa thành insurance.insuranceId -->
            </td>
        `;
        tbody.appendChild(tr);
    });
}
function handleFormSubmit(e) {
    e.preventDefault();

    const id = document.getElementById("insuranceId").value;
    const payload = {
        employeeId: parseInt(document.getElementById("employeeId").value),
        insuranceType: document.getElementById("type").value,
        insuranceAmount: parseFloat(document.getElementById("amount").value),
        startDate: document.getElementById("startDate").value,
        endDate: document.getElementById("endDate").value
    };

    const url = id ? `${API_INSURANCE}/${id}` : API_INSURANCE;
    const method = id ? "PUT" : "POST";

    fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) throw new Error("Không thể lưu dữ liệu");
            return res.json();
        })
        .then(() => {
            alert(id ? "✔️ Cập nhật thành công!" : "✔️ Thêm mới thành công!");
            resetForm();
            showForm(false);
            loadInsurances();
        })
        .catch(err => {
            alert(" Lỗi: " + err.message);
        });
}

function editInsurance(id) {
    fetch(`${API_INSURANCE}/${id}`)
        .then(res => res.json())
        .then(data => {
            const emp = data.employee || {};

            document.getElementById("insuranceId").value = data.insuranceId;  // Sửa lại
            document.getElementById("employeeId").value = emp.employeeId || "";
            document.getElementById("employeeName").value = emp.fullName || "";
            document.getElementById("departmentName").value = emp.department?.departmentName || "";
            document.getElementById("type").value = data.insuranceType;
            document.getElementById("amount").value = data.insuranceAmount;
            document.getElementById("startDate").value = data.startDate || "";
            document.getElementById("endDate").value = data.endDate || "";

            showForm(true);
        })
        .catch(err => {
            console.error(" Không thể tải bảo hiểm:", err);
            alert("Không thể tải dữ liệu bảo hiểm.");
        });
}


function deleteInsurance(id) {
    if (!confirm("Bạn có chắc chắn muốn xoá bảo hiểm này?")) return;

    fetch(`${API_INSURANCE}/${id}`, { method: "DELETE" })
        .then(res => {
            if (!res.ok) throw new Error("Xoá thất bại");
            alert("Đã xoá thành công!");
            loadInsurances();
        })
        .catch(err => {
            alert("Lỗi khi xoá: " + err.message);
        });
}

function fetchEmployeeDetails() {
    const employeeId = document.getElementById("employeeId").value;
    if (!employeeId || isNaN(employeeId)) {
        clearEmployeeInfo();
        return;
    }

    fetch(`${API_EMPLOYEE_DETAIL}/${employeeId}`)
        .then(res => {
            if (!res.ok) throw new Error(`Không tìm thấy nhân viên ID: ${employeeId}`);
            return res.json();
        })
        .then(data => {
            const emp = data.employee;
            if (!emp) throw new Error("Không có thông tin nhân viên");

            document.getElementById("employeeName").value = emp.fullName || "Không xác định";
            document.getElementById("departmentName").value = emp.department?.departmentName || "Không xác định";
        })
        .catch(err => {
            console.warn("⚠️", err.message);
            clearEmployeeInfo();
            alert(" " + err.message);
        });
}


function clearEmployeeInfo() {
    document.getElementById("employeeName").value = "";
    document.getElementById("departmentName").value = "";
}

function exportToExcel() {
    window.open(`${API_INSURANCE}/export`, "_blank");
}

function resetForm() {
    document.getElementById("insuranceForm").reset();
    document.getElementById("insuranceId").value = "";
    clearEmployeeInfo();
}

function showForm(show) {
    const form = document.getElementById("insuranceForm");
    form.style.display = show ? "block" : "none";
}

function debounce(fn, delay) {
    let timeout;
    return (...args) => {
        clearTimeout(timeout);
        timeout = setTimeout(() => fn.apply(this, args), delay);
    };
}
