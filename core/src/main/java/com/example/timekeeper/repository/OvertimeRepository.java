package com.example.timekeeper.repository;

import com.example.timekeeper.entity.OvertimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvertimeRepository extends JpaRepository<OvertimeEntity,Long> {
    @Query("SELECT ot FROM OvertimeEntity ot " +
            "WHERE ot.employee.name = :name " +
            "AND ot.date BETWEEN :startDate AND :endDate")
    List<OvertimeEntity> findByEmployeeNameAndDate(String name, LocalDate startDate, LocalDate endDate);
}
