package com.example.timekeeper.repository;

import com.example.timekeeper.entity.EmployeeEntity;
import com.example.timekeeper.entity.LeaveTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface LeaveTimeRepository extends JpaRepository<LeaveTimeEntity,Long> {

    List<LeaveTimeEntity> findAllByEmployeeId(long id);

    LeaveTimeEntity findByEmployeeIdAndDate(long id, LocalDate date);

    List<LeaveTimeEntity> findAllByEmployeeName(String name);
}
