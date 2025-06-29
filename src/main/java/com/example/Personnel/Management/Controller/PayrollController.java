package com.example.Personnel.Management.Controller;

import com.example.Personnel.Management.DTO.PayrollDTO;
import com.example.Personnel.Management.Service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api/payroll")
public class PayrollController  {

    @Autowired
    private PayrollService payrollService;
    @GetMapping("/list")
    public ResponseEntity<List<PayrollDTO>> getAllPayrolls() {
        List<PayrollDTO> payrolls = payrollService.getAllPayrolls();
        return new ResponseEntity<>(payrolls, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<PayrollDTO> addPayroll(@RequestBody PayrollDTO payrollDTO) {
        PayrollDTO createdPayroll = payrollService.createPayroll(payrollDTO);
        return new ResponseEntity<>(createdPayroll, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PayrollDTO> updatePayroll(@PathVariable Long id, @RequestBody PayrollDTO payrollDTO) {
        PayrollDTO updatedPayroll = payrollService.updatePayroll(id, payrollDTO);
        return new ResponseEntity<>(updatedPayroll, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePayroll(@PathVariable Long id) {
        payrollService.deletePayroll(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PayrollDTO>> searchPayrolls(@RequestParam String keyword) {
        List<PayrollDTO> payrolls = payrollService.searchPayrolls(keyword);
        return new ResponseEntity<>(payrolls, HttpStatus.OK);
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportToExcel() {
        try {
            InputStreamResource file = new InputStreamResource(payrollService.exportPayrollsToExcel());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payrolls.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(file);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
