package com.example.Personnel.Management.Service;

import com.example.Personnel.Management.DTO.AttendanceDTO;

import java.util.List;

public interface AttendanceService {
    /**
     * Check-in cho một nhân viên bằng Employee ID.
     *
     * @param employeeId ID của nhân viên cần Check-in.
     * @return AttendanceDTO chứa thông tin chấm công sau khi Check-in.
     */
    AttendanceDTO checkIn(Long employeeId);

    /**
     * Check-out cho một nhân viên bằng Employee ID.
     *
     * @param employeeId ID của nhân viên cần Check-out.
     * @return AttendanceDTO chứa thông tin chấm công sau khi Check-out.
     */
    AttendanceDTO checkOut(Long employeeId);

    /**
     * Lấy danh sách toàn bộ lịch sử chấm công.
     *
     * @return Danh sách AttendanceDTO chứa thông tin chấm công của tất cả nhân viên.
     */
    List<AttendanceDTO> getAllAttendance();

    /**
     * Lấy thông tin chấm công trong ngày hôm nay của một nhân viên.
     *
     * @param employeeId ID của nhân viên cần lấy thông tin.
     * @return AttendanceDTO chứa thông tin chấm công của nhân viên hôm nay.
     */
    AttendanceDTO getTodayAttendance(Long employeeId);
    /**
     * Lấy Employee ID từ User ID.
     *
     * @param userId ID của User cần tìm Employee ID.
     * @return Employee ID hoặc null nếu không tìm thấy.
     */
    Long getEmployeeByUserId(Long userId);
}