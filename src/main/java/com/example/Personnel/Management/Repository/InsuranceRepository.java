package com.example.Personnel.Management.Repository;

import com.example.Personnel.Management.Entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    List<Insurance> findByEmployee_EmployeeId(Long employeeId);
    List<Insurance> findByInsuranceTypeContainingIgnoreCase(String type);
    void deleteByEmployee_EmployeeId(Long employeeId);

    boolean existsByEmployee_EmployeeId(Long employeeId);
}
