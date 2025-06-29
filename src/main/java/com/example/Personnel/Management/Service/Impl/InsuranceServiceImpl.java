package com.example.Personnel.Management.Service.Impl;

import com.example.Personnel.Management.DTO.InsuranceDTO;
import com.example.Personnel.Management.DTO.InsuranceResponseDTO;
import com.example.Personnel.Management.Entity.Employee;
import com.example.Personnel.Management.Entity.Insurance;
import com.example.Personnel.Management.Repository.EmployeeRepository;
import com.example.Personnel.Management.Repository.InsuranceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import com.example.Personnel.Management.Service.InsuranceService;
@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {
    private final InsuranceRepository insuranceRepository;
    private final EmployeeRepository employeeRepository;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private InsuranceResponseDTO convertToDTO(Insurance insurance) {
        InsuranceResponseDTO dto = new InsuranceResponseDTO();

        dto.setInsuranceId(insurance.getInsuranceId());
        dto.setInsuranceType(insurance.getInsuranceType());
        dto.setInsuranceAmount(insurance.getInsuranceAmount());

        if (insurance.getStartDate() != null) {
            dto.setStartDate(insurance.getStartDate().format(dateFormatter));
        }

        if (insurance.getEndDate() != null) {
            dto.setEndDate(insurance.getEndDate().format(dateFormatter));
        }
        Employee employee = insurance.getEmployee();
        if (employee != null) {
            dto.setEmployeeName(employee.getFullName());

            if (employee.getDepartment() != null) {
                dto.setDepartmentName(employee.getDepartment().getDepartmentName());
                dto.setDepartmentId(employee.getDepartment().getDepartmentId()); // Gán departmentId
            }
        }

        return dto;
    }
    public List<InsuranceResponseDTO> getAllInsurances() {
        return insuranceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public InsuranceResponseDTO getInsuranceDTOById(Long id) {
        Insurance insurance = getInsuranceById(id);
        return convertToDTO(insurance);
    }
    public Insurance getInsuranceById(Long id) {
        return insuranceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("khồng tìm thấy ID bảo hiểm: " + id));
    }
    public Insurance createInsurance(InsuranceDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("không tìm thấy nhân viên có ID: " + dto.getEmployeeId()));

        Insurance insurance = new Insurance();
        insurance.setEmployee(employee);
        insurance.setInsuranceType(dto.getInsuranceType());
        insurance.setInsuranceAmount(dto.getInsuranceAmount());
        insurance.setStartDate(dto.getStartDate());
        insurance.setEndDate(dto.getEndDate());

        return insuranceRepository.save(insurance);
    }
    public Insurance updateInsurance(Long id, InsuranceDTO dto) {
        Insurance insurance = getInsuranceById(id);
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        insurance.setEmployee(employee);
        insurance.setInsuranceType(dto.getInsuranceType());
        insurance.setInsuranceAmount(dto.getInsuranceAmount());
        insurance.setStartDate(dto.getStartDate());
        insurance.setEndDate(dto.getEndDate());

        return insuranceRepository.save(insurance);
    }
    public void deleteInsurance(Long id) {
        if (!insuranceRepository.existsById(id)) {
            throw new RuntimeException("Insurance not found");
        }
        insuranceRepository.deleteById(id);
    }
    public List<InsuranceResponseDTO> searchByEmployeeId(Long employeeId) {
        return insuranceRepository.findByEmployee_EmployeeId(employeeId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<InsuranceResponseDTO> searchByType(String type) {
        return insuranceRepository.findByInsuranceTypeContainingIgnoreCase(type)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public InputStream exportToExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Insurances");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Employee Name");
            header.createCell(2).setCellValue("Department");
            header.createCell(3).setCellValue("Department ID");
            header.createCell(4).setCellValue("Insurance Type");
            header.createCell(5).setCellValue("Amount");
            header.createCell(6).setCellValue("Start Date");
            header.createCell(7).setCellValue("End Date");
            List<Insurance> insurances = insuranceRepository.findAll();
            int rowIdx = 1;
            for (Insurance i : insurances) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(i.getInsuranceId());
                row.createCell(1).setCellValue(i.getEmployee().getFullName());
                row.createCell(2).setCellValue(i.getEmployee().getDepartment().getDepartmentName());
                row.createCell(3).setCellValue(i.getEmployee().getDepartment().getDepartmentId()); // Hiển thị departmentId
                row.createCell(4).setCellValue(i.getInsuranceType());
                row.createCell(5).setCellValue(i.getInsuranceAmount());
                row.createCell(6).setCellValue(i.getStartDate().toString());
                row.createCell(7).setCellValue(i.getEndDate().toString());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("không xuất được excel", e);
        }
    }

}
