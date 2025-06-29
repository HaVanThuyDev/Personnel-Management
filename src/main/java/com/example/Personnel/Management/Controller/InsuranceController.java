package com.example.Personnel.Management.Controller;

import com.example.Personnel.Management.DTO.InsuranceDTO;
import com.example.Personnel.Management.DTO.InsuranceResponseDTO;
import com.example.Personnel.Management.Entity.Insurance;
import com.example.Personnel.Management.Service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/insurances")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;
    @GetMapping
    public ResponseEntity<List<InsuranceResponseDTO>> getAll() {
        return ResponseEntity.ok(insuranceService.getAllInsurances());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(insuranceService.getInsuranceDTOById(id));
    }
    @PostMapping
    public ResponseEntity<Insurance> create(@RequestBody InsuranceDTO dto) {
        Insurance created = insuranceService.createInsurance(dto);
        return ResponseEntity.ok(created);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Insurance> update(@PathVariable Long id, @RequestBody InsuranceDTO dto) {
        Insurance updated = insuranceService.updateInsurance(id, dto);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        insuranceService.deleteInsurance(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search/employee")
    public ResponseEntity<List<InsuranceResponseDTO>> searchByEmployee(@RequestParam Long employeeId) {
        return ResponseEntity.ok(insuranceService.searchByEmployeeId(employeeId));
    }
    @GetMapping("/search/type")
    public ResponseEntity<List<InsuranceResponseDTO>> searchByType(@RequestParam String type) {
        return ResponseEntity.ok(insuranceService.searchByType(type));
    }
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportExcel() {
        InputStreamResource file = new InputStreamResource(insuranceService.exportToExcel());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=insurances.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
}
