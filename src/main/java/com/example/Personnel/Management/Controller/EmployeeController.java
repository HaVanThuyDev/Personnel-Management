package com.example.Personnel.Management.Controller;

import com.example.Personnel.Management.DTO.EmployeeDTO;
import com.example.Personnel.Management.Entity.Employee;
import com.example.Personnel.Management.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employees/api")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<?> getAllEmployees() {
        List<EmployeeDTO> employeeDTOs = employeeService.getAllEmployees()
                .stream()
                .map(employeeService::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("message", "Thành công", "data", employeeDTOs));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(emp -> ResponseEntity.ok(Map.of(
                        "message", "Tìm thấy",
                        "employee", employeeService.convertToDTO(emp)
                )))
                .orElse(ResponseEntity.status(404).body(Map.of(
                        "message", "Không tìm thấy nhân viên"
                )));
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<?> searchEmployees(@RequestParam String name) {
        List<EmployeeDTO> employees = employeeService.searchEmployeesByName(name)
                .stream()
                .map(employeeService::convertToDTO)
                .collect(Collectors.toList());

        if (employees.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy nhân viên nào"));
        }
        return ResponseEntity.ok(Map.of("message", "Tìm thấy", "data", employees));
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDTO dto) {
        try {
            Employee emp = employeeService.createEmployee(dto);
            return ResponseEntity.ok(Map.of("message", "Thêm thành công", "employee", employeeService.convertToDTO(emp)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Lỗi thêm", "error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO dto) {
        try {
            Employee emp = employeeService.updateEmployee(id, dto);
            return ResponseEntity.ok(Map.of("message", "Cập nhật thành công", "employee", employeeService.convertToDTO(emp)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("message", "Không tìm thấy nhân viên", "error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok(Map.of("message", "Xoá thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Lỗi xoá", "error", e.getMessage()));
        }
    }


}
