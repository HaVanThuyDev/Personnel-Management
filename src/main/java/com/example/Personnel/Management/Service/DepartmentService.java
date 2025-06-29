package com.example.Personnel.Management.Service;

import com.example.Personnel.Management.DTO.DepartmentDTO;
import com.example.Personnel.Management.Entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<DepartmentDTO> getAllDepartments();
    Optional<Department> getDepartmentById(Long id);
    Department createDepartment(Department department);
    Department updateDepartment(Long id, Department updated);
    void deleteDepartment(Long id);
    List<DepartmentDTO> searchDepartmentsByName(String keyword);
}
