package com.example.Personnel.Management.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ContractDTO {
    private Long contractId;
    private Long employeeId;
    private String employeeName;
    private String contractType;
    private Date startDate;
    private Date endDate;
    private Double salary;
}
