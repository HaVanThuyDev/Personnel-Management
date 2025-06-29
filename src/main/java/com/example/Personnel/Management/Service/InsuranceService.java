package com.example.Personnel.Management.Service;

import com.example.Personnel.Management.DTO.InsuranceDTO;
import com.example.Personnel.Management.DTO.InsuranceResponseDTO;
import com.example.Personnel.Management.Entity.Insurance;

import java.io.InputStream;
import java.util.List;

public interface InsuranceService {
    List<InsuranceResponseDTO> getAllInsurances();
    InsuranceResponseDTO getInsuranceDTOById(Long id);
    Insurance getInsuranceById(Long id);
    Insurance createInsurance(InsuranceDTO dto);
    Insurance updateInsurance(Long id, InsuranceDTO dto);
    void deleteInsurance(Long id);
    List<InsuranceResponseDTO> searchByEmployeeId(Long employeeId);
    List<InsuranceResponseDTO> searchByType(String type);
    InputStream exportToExcel();
}
