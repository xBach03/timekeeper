package com.example.timekeeper.repository;

import com.example.timekeeper.entity.AttendanceEntity;
import com.example.timekeeper.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity,Long> {
    AttendanceEntity findByEmployeeAndDate(EmployeeEntity employee, LocalDate localDate);

    @Query("SELECT a FROM AttendanceEntity a " +
            "WHERE a.employee.name = :name " +
            "AND a.date BETWEEN :startDate AND :endDate " +
            "ORDER BY a.date ASC")
    List<AttendanceEntity> findWeeklyAttendance(
            @Param("name") String name,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    AttendanceEntity findByEmployeeNameAndDate(String name, LocalDate date);
}
