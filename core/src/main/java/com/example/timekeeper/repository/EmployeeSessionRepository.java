package com.example.timekeeper.repository;

import com.example.timekeeper.entity.EmployeeSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSessionRepository extends JpaRepository<EmployeeSessionEntity,Long> {
    EmployeeSessionEntity findByEmployeeName(String name);
}
