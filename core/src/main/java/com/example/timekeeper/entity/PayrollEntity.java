package com.example.timekeeper.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

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
    private float totalSalary;

    @Column(name = "tax")
    private float tax;

    @Column(name = "bonus")
    private float bonus;

    @Column(name = "hours_worked")
    private float hoursWorked;

    @Column(name = "hourlyRate")
    private float hourlyRate;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "period")
    private String period;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference(value = "employee-payroll")
    private EmployeeEntity employee;
}
