package com.example.Personnel.Management.DTO;

import lombok.Data;
import java.util.Date;

@Data
public class AttendanceDTO {
    private Long attendanceId;
    private Long employeeId;
    private String fullName;
    private Date date;
    private Date checkIn;
    private Date checkOut;
    private Double workHours;
}
