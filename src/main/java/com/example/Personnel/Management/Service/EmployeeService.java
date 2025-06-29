package com.example.Personnel.Management.Service;

import com.example.Personnel.Management.DTO.EmployeeDTO;
import com.example.Personnel.Management.Entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long id);
    List<Employee> searchEmployeesByName(String name);
    Employee createEmployee(EmployeeDTO dto);
    Employee updateEmployee(Long id, EmployeeDTO dto);
    void deleteEmployee(Long id);
    EmployeeDTO convertToDTO(Employee employee);
}
