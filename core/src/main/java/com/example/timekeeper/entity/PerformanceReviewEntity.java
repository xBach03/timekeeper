package com.example.timekeeper.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "performance_review")
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class PerformanceReviewEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "period")
    private String period;

    @Column(name = "score")
    private float score;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference(value = "employee-review")
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonBackReference(value = "manager-review")
    private ManagerEntity manager;
}
