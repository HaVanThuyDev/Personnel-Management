package com.example.Personnel.Management.Repository;

import com.example.Personnel.Management.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByDepartmentNameContainingIgnoreCase(String name);

}
