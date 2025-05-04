package com.example.timekeeper.service;

import com.example.timekeeper.entity.LeaveTimeEntity;
import com.example.timekeeper.repository.LeaveTimeRepository;
import org.springframework.stereotype.Service;

@Service
public class LeaveTimeService {
    private final LeaveTimeRepository leaveTimeRepository;

    public LeaveTimeService(LeaveTimeRepository leaveTimeRepository) {
        this.leaveTimeRepository = leaveTimeRepository;
    }

    public void save(LeaveTimeEntity leaveTime) {
        leaveTimeRepository.save(leaveTime);
    }
}
