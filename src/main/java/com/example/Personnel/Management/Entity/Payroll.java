package com.example.Personnel.Management.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payrolls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_id")
    private Long payrollId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "basic_salary", nullable = false)
    private Double basicSalary;

    @Column(name = "overtime_pay", nullable = false)
    private Double overtimePay;

    @Column(name = "final_salary", nullable = false)
    private Double finalSalary;
}
