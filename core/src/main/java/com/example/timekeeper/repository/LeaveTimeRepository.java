package com.example.timekeeper.repository;

import com.example.timekeeper.entity.LeaveTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveTimeRepository extends JpaRepository<LeaveTimeEntity, Long> {
}
