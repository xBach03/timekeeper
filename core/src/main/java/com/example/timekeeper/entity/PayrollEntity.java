package com.example.timekeeper.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "payroll")
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class PayrollEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "total_salary")
    private Long totalSalary;

    @Column(name = "tax")
    private Long tax;

    @Column(name = "bonus")
    private Long bonus;

    @Column(name = "period")
    private String period;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference(value = "employee-payroll")
    private EmployeeEntity employee;
}
