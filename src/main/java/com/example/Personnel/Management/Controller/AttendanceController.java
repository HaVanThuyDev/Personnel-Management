package com.example.Personnel.Management.Controller;

import com.example.Personnel.Management.DTO.AttendanceDTO;
import com.example.Personnel.Management.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/employee-id/{userId}")
    public ResponseEntity<?> getEmployeeIdByUserId(@PathVariable Long userId) {
        try {
            Long employeeId = attendanceService.getEmployeeByUserId(userId);
            return ResponseEntity.ok(Map.of("employeeId", employeeId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return serverError();
        }
    }

    @PostMapping("/check-in/{userId}")
    public ResponseEntity<?> checkIn(@PathVariable Long userId) {
        try {
            AttendanceDTO dto = attendanceService.checkIn(userId);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return serverError();
        }
    }

    @PostMapping("/check-out/{userId}")
    public ResponseEntity<?> checkOut(@PathVariable Long userId) {
        try {
            AttendanceDTO dto = attendanceService.checkOut(userId);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return serverError();
        }
    }

    @GetMapping("/today/{userId}")
    public ResponseEntity<?> getTodayAttendance(@PathVariable Long userId) {
        try {
            AttendanceDTO dto = attendanceService.getTodayAttendance(userId);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return serverError();
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAttendance() {
        try {
            List<AttendanceDTO> list = attendanceService.getAllAttendance();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return serverError();
        }
    }
    private ResponseEntity<Map<String, String>> serverError() {
        return ResponseEntity.status(500).body(Map.of("message", "Internal server error"));
    }
}