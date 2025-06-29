package com.example.Personnel.Management.DTO;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollDTO {
    private Long payrollId;
    private Long employeeId;
    private String fullName;
    private int month;
    private int year;
    private Double basicSalary;
    private Double overtimePay;
    private Double finalSalary;
}
