package com.example.Personnel.Management.Service.Impl;

import com.example.Personnel.Management.Repository.EmployeeRepository;
import com.example.Personnel.Management.Repository.DepartmentRepository;
import com.example.Personnel.Management.Repository.UserRepository;
import com.example.Personnel.Management.Repository.ContractRepository;
import com.example.Personnel.Management.Repository.PayrollRepository;
import com.example.Personnel.Management.Service.StatisticService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private PayrollRepository payrollRepository;

    @Override
    public long getTotalEmployees() {
        return employeeRepository.count();
    }

    @Override
    public long getTotalDepartments() {
        return departmentRepository.count();
    }

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalContracts() {
        return contractRepository.count();
    }

    @Override
    public long getTotalPayrollAmount() {
        return (long) payrollRepository.findAll()
                .stream()
                .mapToDouble(p -> p.getFinalSalary() != null ? p.getFinalSalary() : 0.0)
                .sum();
    }
}
