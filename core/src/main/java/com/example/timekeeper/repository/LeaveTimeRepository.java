package com.example.timekeeper.repository;

import com.example.timekeeper.entity.LeaveTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface LeaveTimeRepository extends JpaRepository<LeaveTimeEntity,Long> {

    List<LeaveTimeEntity> findAllByEmployeeId(long id);

    LeaveTimeEntity findByEmployeeIdAndDate(long id, LocalDate date);

    List<LeaveTimeEntity> findAllByEmployeeName(String name);

    @Query("SELECT lt FROM LeaveTimeEntity lt " +
            "WHERE lt.employee.name = :name " +
            "AND lt.date BETWEEN :startDate AND :endDate " +
            "AND lt.status = :status " +
            "ORDER BY lt.date ASC")
    List<LeaveTimeEntity> findWeeklyLeaves(
            @Param("name") String name,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") String status
    );
}
