package com.example.Personnel.Management.Service;

import com.example.Personnel.Management.DTO.PayrollDTO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface PayrollService {
    List<PayrollDTO> getAllPayrolls();
    PayrollDTO getPayrollById(Long id);
    PayrollDTO createPayroll(PayrollDTO dto);
    PayrollDTO updatePayroll(Long id, PayrollDTO dto);
    void deletePayroll(Long id);
    List<PayrollDTO> searchPayrolls(String keyword);
    ByteArrayInputStream exportPayrollsToExcel() throws IOException;
}
