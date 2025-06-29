const apiUrl = 'http://localhost:8080/api/contracts';

async function loadContracts() {
    const res = await fetch(apiUrl);
    const contracts = await res.json();

    const tableBody = document.querySelector("#contractTable tbody");
    tableBody.innerHTML = "";

    contracts.forEach(contract => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${contract.contractId || 'N/A'}</td>
            <td>${contract.employeeId || 'N/A'}</td>
            <td>${contract.employeeName || 'N/A'}</td>
            <td>${contract.contractType || 'N/A'}</td>
            <td>${contract.endDate || ''}</td>
            <td>${contract.startDate}</td>
            <td>${formatCurrency(contract.salary)}</td>
            <td>
                <button onclick="editContract(${contract.contractId})">✏️ Sửa</button>
                <button onclick="deleteContract(${contract.contractId})">🗑️ Xóa</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}
function showForm() {
    document.getElementById("contractForm").reset();
    document.getElementById("contractId").value = "";
    document.getElementById("formTitle").innerText = "Thêm Hợp đồng";
    document.getElementById("formContainer").classList.remove("hidden");
}
function hideForm() {
    document.getElementById("formContainer").classList.add("hidden");
}
async function submitForm(event) {
    event.preventDefault();

    const id = document.getElementById("contractId").value;
    const salary = document.getElementById("salary").value;
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value || null;
    const contractType = document.getElementById("contractType").value;
    const employeeId = document.getElementById("employeeId").value;

    if (!salary || !startDate || !contractType || !employeeId) {
        alert("Vui lòng nhập đầy đủ thông tin.");
        return;
    }

    const contractData = {
        salary: parseFloat(salary),
        startDate,
        endDate,
        contractType,
        employeeId: parseInt(employeeId)
    };

    let method = "POST";
    let url = apiUrl;

    if (id) {
        method = "PUT";
        url = `${apiUrl}/${id}`;
    }

    const res = await fetch(url, {
        method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(contractData)
    });

    if (res.ok) {
        hideForm();
        loadContracts();
    } else {
        alert("Lỗi khi lưu hợp đồng");
    }
}
async function editContract(id) {
    const res = await fetch(`${apiUrl}/${id}`);
    if (!res.ok) {
        alert("Không tìm thấy hợp đồng");
        return;
    }
    const contract = await res.json();
    document.getElementById("contractId").value = contract.contractId;
    document.getElementById("salary").value = contract.salary;
    document.getElementById("startDate").value = contract.startDate.substring(0, 10);
    document.getElementById("endDate").value = contract.endDate ? contract.endDate.substring(0, 10) : '';
    document.getElementById("contractType").value = contract.contractType;
    document.getElementById("employeeId").value = contract.employeeId;

    document.getElementById("formTitle").innerText = "Chỉnh sửa Hợp đồng";
    document.getElementById("formContainer").classList.remove("hidden");
}
async function deleteContract(id) {
    if (confirm("Bạn có chắc muốn xóa hợp đồng này?")) {
        const res = await fetch(`${apiUrl}/${id}`, { method: "DELETE" });
        if (res.ok) {
            loadContracts();
        } else {
            alert("Lỗi khi xóa hợp đồng");
        }
    }
}
async function searchContract() {
    const searchId = document.getElementById("searchInput").value.trim();
    if (!searchId) {
        loadContracts();
        return;
    }

    const res = await fetch(`${apiUrl}/${searchId}`);
    if (res.ok) {
        const contract = await res.json();
        const tableBody = document.querySelector("#contractTable tbody");
        tableBody.innerHTML = "";

        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${contract.contractId || 'N/A'}</td>
            <td>${contract.employeeId || 'N/A'}</td>
            <td>${contract.employeeName || 'N/A'}</td>
            <td>${contract.contractType || 'N/A'}</td>
            <td>${contract.endDate || ''}</td>
            <td>${contract.startDate}</td>
            <td>${formatCurrency(contract.salary)}</td>
            <td>
                <button onclick="editContract(${contract.contractId})">✏️ Sửa</button>
                <button onclick="deleteContract(${contract.contractId})">🗑️ Xóa</button>
            </td>
        `;
        tableBody.appendChild(row);
    } else {
        alert("Không tìm thấy hợp đồng");
    }
}
function formatCurrency(value) {
    if (!value) return '0 đ';
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
}
loadContracts();
