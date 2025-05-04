package com.example.timekeeper.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Entity
@Table(name = "overtime")
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class OvertimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total_hour")
    private Short totalHour;

    @Column(name = "project")
    private String project;

    @Column(name = "approval")
    private String approval;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference(value = "employee-overtime")
    private EmployeeEntity employee;
}

