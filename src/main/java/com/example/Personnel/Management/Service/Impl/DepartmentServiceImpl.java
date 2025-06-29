package com.example.Personnel.Management.Service.Impl;

import com.example.Personnel.Management.DTO.DepartmentDTO;
import com.example.Personnel.Management.Entity.Department;
import com.example.Personnel.Management.Entity.Employee;
import com.example.Personnel.Management.Repository.DepartmentRepository;
import com.example.Personnel.Management.Repository.EmployeeRepository;
import com.example.Personnel.Management.Service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Department createDepartment(Department department) {
        if (department.getManager() != null && department.getManager().getEmployeeId() != null) {
            Employee manager = employeeRepository.findById(department.getManager().getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("quản lí không tồn tại."));
            department.setManager(manager);
        } else {
            department.setManager(null);
        }
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Long id, Department updated) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phòng ban không tồn tại"));
        existing.setDepartmentName(updated.getDepartmentName());
        if (updated.getManager() != null && updated.getManager().getEmployeeId() != null) {
            Employee manager = employeeRepository.findById(updated.getManager().getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("quản lí không tồn tại."));
            existing.setManager(manager);
        } else {
            existing.setManager(null);
        }
        return departmentRepository.save(existing);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public List<DepartmentDTO> searchDepartmentsByName(String keyword) {
        return departmentRepository.findByDepartmentNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setDepartmentId(department.getDepartmentId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setManagerName(department.getManager() != null
                ? department.getManager().getFullName() + " (#" + department.getManager().getEmployeeId() + ")"
                : "Chưa có");
        return dto;
    }
}
