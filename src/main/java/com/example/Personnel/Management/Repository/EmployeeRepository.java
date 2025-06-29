package com.example.Personnel.Management.Repository;
import com.example.Personnel.Management.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByFullNameContainingIgnoreCase(String fullName);
    void deleteByUserId(Long userId);
    Optional<Employee> findByUserId(Long userId);
}
