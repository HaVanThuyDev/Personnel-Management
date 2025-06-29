package com.example.Personnel.Management.Service.Impl;

import com.example.Personnel.Management.DTO.ContractDTO;
import com.example.Personnel.Management.Entity.Contract;
import com.example.Personnel.Management.Entity.Employee;
import com.example.Personnel.Management.Repository.ContractRepository;
import com.example.Personnel.Management.Repository.EmployeeRepository;
import com.example.Personnel.Management.Service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Contract getContractById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    @Override
    public List<ContractDTO> getAllContractDTOs() {
        List<Contract> contracts = contractRepository.findAll();

        return contracts.stream().map(contract -> {
            ContractDTO dto = new ContractDTO();
            dto.setContractId(contract.getContractId());
            dto.setEmployeeId(contract.getEmployee().getEmployeeId());
            dto.setEmployeeName(contract.getEmployee().getFullName());
            dto.setContractType(contract.getContractType());
            dto.setStartDate(contract.getStartDate());
            dto.setEndDate(contract.getEndDate());
            dto.setSalary(contract.getSalary());
            return dto;
        }).toList();
    }

    @Override
    public Contract createContract(ContractDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Contract contract = new Contract();
        contract.setEmployee(employee);
        contract.setContractType(dto.getContractType());
        contract.setStartDate(dto.getStartDate());
        contract.setEndDate(dto.getEndDate());
        contract.setSalary(dto.getSalary());

        return contractRepository.save(contract);
    }

    @Override
    public Contract updateContract(Long id, ContractDTO dto) {
        Contract existing = getContractById(id);

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        existing.setEmployee(employee);
        existing.setContractType(dto.getContractType());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setSalary(dto.getSalary());

        return contractRepository.save(existing);
    }

    @Override
    public void deleteContract(Long id) {
        if (!contractRepository.existsById(id)) {
            throw new RuntimeException("Contract not found");
        }
        contractRepository.deleteById(id);
    }
}
