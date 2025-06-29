package com.example.Personnel.Management.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;
    @Column(name = "department_name", nullable = false, unique = true)
    private String departmentName;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
}
