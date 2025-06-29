package com.example.Personnel.Management.Service.Impl;

import com.example.Personnel.Management.DTO.PayrollDTO;
import com.example.Personnel.Management.Entity.Employee;
import com.example.Personnel.Management.Entity.Payroll;
import com.example.Personnel.Management.Repository.EmployeeRepository;
import com.example.Personnel.Management.Repository.PayrollRepository;
import com.example.Personnel.Management.Service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<PayrollDTO> getAllPayrolls() {
        return payrollRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PayrollDTO getPayrollById(Long id) {
        return payrollRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản lương!"));
    }

    @Override
    public PayrollDTO createPayroll(PayrollDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên!"));
        Payroll payroll = Payroll.builder()
                .employee(employee)
                .month(dto.getMonth())
                .year(dto.getYear())
                .basicSalary(dto.getBasicSalary())
                .overtimePay(dto.getOvertimePay())
                .finalSalary(dto.getFinalSalary())
                .build();
        return convertToDTO(payrollRepository.save(payroll));
    }

    @Override
    public PayrollDTO updatePayroll(Long id, PayrollDTO dto) {
        Payroll existing = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản lương!"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên!"));
        existing.setEmployee(employee);
        existing.setMonth(dto.getMonth());
        existing.setYear(dto.getYear());
        existing.setBasicSalary(dto.getBasicSalary());
        existing.setOvertimePay(dto.getOvertimePay());
        existing.setFinalSalary(dto.getFinalSalary());
        return convertToDTO(payrollRepository.save(existing));
    }

    @Override
    public void deletePayroll(Long id) {
        if (!payrollRepository.existsById(id)) {
            throw new RuntimeException("Không tồn tại bản lương!");
        }
        payrollRepository.deleteById(id);
    }

    @Override
    public List<PayrollDTO> searchPayrolls(String keyword) {
        List<Payroll> payrolls = payrollRepository.findAll();
        return payrolls.stream()
                .filter(payroll -> payroll.getEmployee().getFullName().toLowerCase().contains(keyword.toLowerCase()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ByteArrayInputStream exportPayrollsToExcel() throws IOException {
        List<Payroll> payrolls = payrollRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Payrolls");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue(" ID");
        headerRow.createCell(1).setCellValue(" ID Nhân viên");
        headerRow.createCell(2).setCellValue("Họ và tên");
        headerRow.createCell(3).setCellValue("Tháng");
        headerRow.createCell(4).setCellValue("Năm");
        headerRow.createCell(5).setCellValue("Lương cơ bản");
        headerRow.createCell(6).setCellValue("Lương tăng ca");
        headerRow.createCell(7).setCellValue("Tổng lương");
        int rowNum = 1;
        for (Payroll payroll : payrolls) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(payroll.getPayrollId());
            row.createCell(1).setCellValue(payroll.getEmployee().getEmployeeId());
            row.createCell(2).setCellValue(payroll.getEmployee().getFullName());
            row.createCell(3).setCellValue(payroll.getMonth());
            row.createCell(4).setCellValue(payroll.getYear());
            row.createCell(5).setCellValue(payroll.getBasicSalary());
            row.createCell(6).setCellValue(payroll.getOvertimePay());
            row.createCell(7).setCellValue(payroll.getFinalSalary());
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    private PayrollDTO convertToDTO(Payroll payroll) {
        return PayrollDTO.builder()
                .payrollId(payroll.getPayrollId())
                .employeeId(payroll.getEmployee().getEmployeeId())
                .fullName(payroll.getEmployee().getFullName())
                .month(payroll.getMonth())
                .year(payroll.getYear())
                .basicSalary(payroll.getBasicSalary())
                .overtimePay(payroll.getOvertimePay())
                .finalSalary(payroll.getFinalSalary())
                .build();
    }
}