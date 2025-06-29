package com.example.Personnel.Management.Repository;

import com.example.Personnel.Management.Entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByMonthAndYear(int month, int year);
    List<Payroll> findByEmployee_EmployeeId(Long employeeId);
    void deleteByEmployee_EmployeeId(Long employeeId);
    boolean existsByEmployee_EmployeeId(Long employeeId);
}
