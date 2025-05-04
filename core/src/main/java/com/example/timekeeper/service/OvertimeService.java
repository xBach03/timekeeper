package com.example.timekeeper.service;

import com.example.timekeeper.entity.OvertimeEntity;
import com.example.timekeeper.repository.OvertimeRepository;
import org.springframework.stereotype.Service;

@Service
public class OvertimeService {
    private final OvertimeRepository overtimeRepository;

    public OvertimeService(OvertimeRepository overtimeRepository) {
        this.overtimeRepository = overtimeRepository;
    }

    public void save(OvertimeEntity overtime) {
        overtimeRepository.save(overtime);
    }
}
