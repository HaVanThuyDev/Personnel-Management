package com.example.Personnel.Management.Controller;

import com.example.Personnel.Management.DTO.ContractDTO;
import com.example.Personnel.Management.Entity.Contract;
import com.example.Personnel.Management.Service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    public ResponseEntity<List<ContractDTO>> getAllContracts() {
        List<ContractDTO> contracts = contractService.getAllContractDTOs();
        return ResponseEntity.ok(contracts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractDTO> getContractById(@PathVariable Long id) {
        Contract contract = contractService.getContractById(id);
        ContractDTO dto = new ContractDTO();
        dto.setContractId(contract.getContractId());
        dto.setEmployeeId(contract.getEmployee().getEmployeeId());
        dto.setEmployeeName(contract.getEmployee().getFullName());
        dto.setContractType(contract.getContractType());
        dto.setStartDate(contract.getStartDate());
        dto.setEndDate(contract.getEndDate());
        dto.setSalary(contract.getSalary());

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ContractDTO> createContract(@RequestBody ContractDTO dto) {
        Contract saved = contractService.createContract(dto);

        ContractDTO result = new ContractDTO();
        result.setContractId(saved.getContractId());
        result.setEmployeeId(saved.getEmployee().getEmployeeId());
        result.setEmployeeName(saved.getEmployee().getFullName());
        result.setContractType(saved.getContractType());
        result.setStartDate(saved.getStartDate());
        result.setEndDate(saved.getEndDate());
        result.setSalary(saved.getSalary());

        return ResponseEntity
                .created(URI.create("/api/contracts/" + saved.getContractId()))
                .body(result);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ContractDTO> updateContract(@PathVariable Long id, @RequestBody ContractDTO dto) {
        Contract updated = contractService.updateContract(id, dto);

        ContractDTO result = new ContractDTO();
        result.setContractId(updated.getContractId());
        result.setEmployeeId(updated.getEmployee().getEmployeeId());
        result.setEmployeeName(updated.getEmployee().getFullName());
        result.setContractType(updated.getContractType());
        result.setStartDate(updated.getStartDate());
        result.setEndDate(updated.getEndDate());
        result.setSalary(updated.getSalary());

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}
