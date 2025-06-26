package com.example.timekeeper.repository;

import com.example.timekeeper.entity.PayrollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<PayrollEntity,Long> {
    @Query("SELECT pr FROM PayrollEntity pr " +
            "WHERE pr.employee.name = :name " +
            "AND pr.date BETWEEN :startDate AND :endDate")
    List<PayrollEntity> getByEmployeeNameAndDate(String name, LocalDate startDate, LocalDate endDate);
}
