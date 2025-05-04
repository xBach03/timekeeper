package com.example.timekeeper.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "leave_time")
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class LeaveTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "total_hour")
    private Short totalHour;

    @Column(name = "category")
    private String category;

    @Column(name = "approval")
    private String approval;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference(value = "employee-leaveTime")
    private EmployeeEntity employee;

}
