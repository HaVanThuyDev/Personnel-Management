package com.example.Personnel.Management.Service;

import com.example.Personnel.Management.DTO.ContractDTO;
import com.example.Personnel.Management.Entity.Contract;
import java.util.List;

public interface ContractService {
    Contract getContractById(Long id);
    List<ContractDTO> getAllContractDTOs();
    Contract createContract(ContractDTO dto);
    Contract updateContract(Long id, ContractDTO dto);
    void deleteContract(Long id);
}
