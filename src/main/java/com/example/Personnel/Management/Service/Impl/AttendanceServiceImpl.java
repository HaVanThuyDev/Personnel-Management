package com.example.Personnel.Management.Service.Impl;

import com.example.Personnel.Management.DTO.AttendanceDTO;
import com.example.Personnel.Management.Entity.Attendance;
import com.example.Personnel.Management.Entity.Employee;
import com.example.Personnel.Management.Repository.AttendanceRepository;
import com.example.Personnel.Management.Repository.EmployeeRepository;
import com.example.Personnel.Management.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public AttendanceDTO checkIn(Long userId) {
        Employee employee = getEmployee(userId);
        Date today = getVietnamDateOnly();

        Optional<Attendance> optional = attendanceRepository.findByEmployeeAndDate(employee, today);
        if (optional.isPresent()) {
            Attendance att = optional.get();
            if (att.getCheckIn() != null) {
                throw new RuntimeException("Đã check-in lúc: " + formatTime(att.getCheckIn()));
            }
            att.setCheckIn(getNowVietnamTime());
            attendanceRepository.save(att);
            Attendance freshAtt = attendanceRepository.findById(att.getAttendanceId()).orElse(att);
            return toDTO(freshAtt);
        }

        Attendance newAtt = new Attendance();
        newAtt.setEmployee(employee);
        newAtt.setDate(today);
        newAtt.setCheckIn(getNowVietnamTime());
        Attendance savedAtt = attendanceRepository.save(newAtt);
        Attendance freshAtt = attendanceRepository.findById(savedAtt.getAttendanceId()).orElse(savedAtt);
        return toDTO(freshAtt);
    }

    @Override
    public AttendanceDTO checkOut(Long userId) {
        Employee employee = getEmployee(userId);
        Date today = getVietnamDateOnly();

        Attendance att = attendanceRepository.findByEmployeeAndDate(employee, today)
                .orElseThrow(() -> new RuntimeException("Chưa check-in hôm nay"));

        if (att.getCheckOut() != null) {
            throw new RuntimeException("Đã check-out lúc: " + formatTime(att.getCheckOut()));
        }

        Date checkOutTime = getNowVietnamTime();
        att.setCheckOut(checkOutTime);

        if (att.getCheckIn() != null) {
            long durationMs = checkOutTime.getTime() - att.getCheckIn().getTime();
            double hours = durationMs / (1000.0 * 60 * 60);
            att.setWorkHours(Math.round(hours * 100.0) / 100.0);
        }

        attendanceRepository.save(att);
        Attendance freshAtt = attendanceRepository.findById(att.getAttendanceId()).orElse(att);
        return toDTO(freshAtt);
    }

    @Override
    public AttendanceDTO getTodayAttendance(Long userId) {
        Employee employee = getEmployee(userId);
        Date today = getVietnamDateOnly();

        return attendanceRepository.findByEmployeeAndDate(employee, today)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Không có dữ liệu hôm nay"));
    }

    @Override
    public List<AttendanceDTO> getAllAttendance() {
        return attendanceRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Long getEmployeeByUserId(Long userId) {
        return employeeRepository.findByUserId(userId)
                .map(Employee::getEmployeeId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên với userId: " + userId));
    }

    // =========================
    // ✨ Tiện ích nội bộ ✨
    // =========================

    private Employee getEmployee(Long userId) {
        Long employeeId = getEmployeeByUserId(userId);
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + employeeId));
    }

    private Date getNowVietnamTime() {
        return Date.from(ZonedDateTime.now(VIETNAM_ZONE).toInstant());
    }

    private Date getVietnamDateOnly() {
        return Date.from(LocalDate.now(VIETNAM_ZONE).atStartOfDay(VIETNAM_ZONE).toInstant());
    }

    private AttendanceDTO toDTO(Attendance attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setAttendanceId(attendance.getAttendanceId());
        dto.setEmployeeId(attendance.getEmployee().getEmployeeId());
        dto.setFullName(attendance.getEmployee().getFullName());
        dto.setDate(attendance.getDate());
        dto.setCheckIn(attendance.getCheckIn());   // <-- Gán trực tiếp kiểu Date
        dto.setCheckOut(attendance.getCheckOut()); // <-- Gán trực tiếp kiểu Date
        dto.setWorkHours(attendance.getWorkHours());
        return dto;
    }
    // Chuyển Date -> String HH:mm:ss theo múi giờ Việt Nam
    private String formatTime(Date date) {
        if (date == null) return null;
        Instant instant = date.toInstant();
        LocalTime time = instant.atZone(VIETNAM_ZONE).toLocalTime();
        return time.format(TIME_FORMATTER);
    }
}