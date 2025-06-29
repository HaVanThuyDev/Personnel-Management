package com.example.Personnel.Management.Repository;

import com.example.Personnel.Management.Entity.Attendance;
import com.example.Personnel.Management.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeAndDate(Employee employee, Date date);
    void deleteByEmployee_EmployeeId(Long employeeId);
    boolean existsByEmployee_EmployeeId(Long employeeId);




}
