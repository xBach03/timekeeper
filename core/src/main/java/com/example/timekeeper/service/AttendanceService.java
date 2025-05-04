package com.example.timekeeper.service;

import com.example.timekeeper.entity.AttendanceEntity;
import com.example.timekeeper.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }
    public void save(AttendanceEntity attendance) {
        attendanceRepository.save(attendance);
    }
}
