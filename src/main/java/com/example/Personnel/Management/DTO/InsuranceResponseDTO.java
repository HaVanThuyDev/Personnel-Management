package com.example.Personnel.Management.DTO;

import lombok.Data;

@Data
public class InsuranceResponseDTO {
    private Long insuranceId;
    private String insuranceType;
    private Double insuranceAmount;
    private String startDate;
    private String endDate;
    private String employeeName;
    private String departmentName;
    private Long departmentId;
}
