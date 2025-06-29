package com.example.Personnel.Management.Repository;

import com.example.Personnel.Management.Entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    void deleteByEmployee_EmployeeId(Long employeeId);

    boolean existsByEmployee_EmployeeId(Long employeeId);
}
