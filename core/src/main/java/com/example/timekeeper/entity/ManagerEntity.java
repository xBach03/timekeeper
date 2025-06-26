package com.example.timekeeper.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "manager")
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class ManagerEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "level")
    private String level;

    @Column(name = "team_size")
    private Short teamSize;

    @Column(name = "review_count")
    private long reviewCount;

    @Column(name = "average_review_score")
    private float averageReviewScore;

    @OneToOne(mappedBy = "manager")
    @JsonManagedReference(value = "manager-department")
    private DepartmentEntity department;

    @OneToMany(
            mappedBy = "manager",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference(value = "manager-employee")
    private List<EmployeeEntity> employeeList;

    @OneToMany(
            mappedBy = "manager",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference(value = "manager-review")
    private List<PerformanceReviewEntity> perFormanceReviewList;

}
