package com.example.Personnel.Management.Service.Impl;

import com.example.Personnel.Management.DTO.EmployeeDTO;
import com.example.Personnel.Management.Entity.Department;
import com.example.Personnel.Management.Entity.Employee;
import com.example.Personnel.Management.Entity.User;
import com.example.Personnel.Management.Repository.*;
import com.example.Personnel.Management.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final ContractRepository contractRepository;
    private final InsuranceRepository insuranceRepository;
    private final PayrollRepository payrollRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }
    @Override
    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByFullNameContainingIgnoreCase(name);
    }
    @Override
    public Employee createEmployee(EmployeeDTO dto) {
        Employee employee = mapToEntity(dto);
        return employeeRepository.save(employee);
    }
    @Override
    public Employee updateEmployee(Long id, EmployeeDTO dto) {
        return employeeRepository.findById(id)
                .map(existing -> {
                    updateEntityFromDTO(existing, dto);
                    return employeeRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }
    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        if (insuranceRepository.existsByEmployee_EmployeeId(id)) {
            insuranceRepository.deleteByEmployee_EmployeeId(id);
        }
        if (contractRepository.existsByEmployee_EmployeeId(id)) {
            contractRepository.deleteByEmployee_EmployeeId(id);
        }
        if (attendanceRepository.existsByEmployee_EmployeeId(id)) {
            attendanceRepository.deleteByEmployee_EmployeeId(id);
        }
        if (payrollRepository.existsByEmployee_EmployeeId(id)) {
            payrollRepository.deleteByEmployee_EmployeeId(id);
        }
        employeeRepository.deleteById(id);
    }
    private Employee mapToEntity(EmployeeDTO dto) {
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getDepartmentId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        return Employee.builder()
                .fullName(dto.getFullName())
                .dob(dto.getDob())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .hireDate(dto.getHireDate())
                .department(department)
                .user(user)
                .build();
    }
    private void updateEntityFromDTO(Employee employee, EmployeeDTO dto) {
        employee.setFullName(dto.getFullName());
        employee.setDob(dto.getDob());
        employee.setGender(dto.getGender());
        employee.setPhone(dto.getPhone());
        employee.setEmail(dto.getEmail());
        employee.setAddress(dto.getAddress());
        employee.setHireDate(dto.getHireDate());
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getDepartmentId()));
        employee.setDepartment(department);
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
        employee.setUser(user);
    }
    @Override
    public EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFullName(employee.getFullName());
        dto.setDob(employee.getDob());
        dto.setGender(employee.getGender());
        dto.setPhone(employee.getPhone());
        dto.setEmail(employee.getEmail());
        dto.setAddress(employee.getAddress());
        dto.setHireDate(employee.getHireDate());

        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getDepartmentId());
            dto.setDepartmentName(employee.getDepartment().getDepartmentName());
        }
        if (employee.getUser() != null) {
            dto.setUserId(employee.getUser().getId());
            dto.setUsername(employee.getUser().getUsername());
        }
        return dto;
    }
}
