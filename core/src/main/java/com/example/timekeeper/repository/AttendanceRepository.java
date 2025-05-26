package com.example.timekeeper.repository;

import com.example.timekeeper.entity.AttendanceEntity;
import com.example.timekeeper.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity,Long> {
    AttendanceEntity findByEmployeeAndDate(EmployeeEntity employee, LocalDate localDate);
}
