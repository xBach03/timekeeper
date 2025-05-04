package com.example.timekeeper.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "department")
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class DepartmentEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "field")
    private String field;

    @OneToOne
    @JoinColumn(name = "manager_id")
    @JsonBackReference(value = "manager-department")
    private ManagerEntity manager;
}
