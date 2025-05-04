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

    @Column(name = "title")
    private String title;

    @Column(name = "team_size")
    private Short teamSize;

    @OneToOne(mappedBy = "manager")
    @JsonManagedReference(value = "manager-department")
    private DepartmentEntity department;

    @OneToMany(
            mappedBy = "manager",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference(value = "manager-employee")
    private List<EmployeeEntity> employeeList;

}
