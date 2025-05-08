package com.example.timekeeper.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employee")
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class EmployeeEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "age")
    private Short age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "dob")
    private Date dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonBackReference(value = "department-employee")
    private DepartmentEntity department;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonBackReference(value = "manager-employee")
    private ManagerEntity manager;

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference(value = "employee-attendance")
    private List<AttendanceEntity> attendanceList;

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference(value = "employee-leaveTime")
    private List<LeaveTimeEntity> leaveTimeList;

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference(value = "employee-overtime")
    private List<OvertimeEntity> overtimeList;

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference(value = "employee-payroll")
    private List<PayrollEntity> payrollList;
}
