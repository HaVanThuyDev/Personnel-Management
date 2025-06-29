package com.example.Personnel.Management.DTO;

import lombok.Data;
import java.util.Date;

@Data
public class EmployeeDTO {
    private Long employeeId;
    private String fullName;
    private Date dob;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private Date hireDate;
    private Long departmentId;
    private String departmentName;
    private Long userId;
    private String username;
}
