package com.example.timekeeper.repository;

import com.example.timekeeper.entity.OvertimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OvertimeRepository extends JpaRepository<OvertimeEntity, Long> {
}
