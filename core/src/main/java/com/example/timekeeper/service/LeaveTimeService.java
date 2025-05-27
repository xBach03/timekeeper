package com.example.timekeeper.service;

import com.example.timekeeper.entity.EmployeeEntity;
import com.example.timekeeper.entity.LeaveTimeEntity;
import com.example.timekeeper.repository.EmployeeRepository;
import com.example.timekeeper.repository.LeaveTimeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveTimeService {
    private final LeaveTimeRepository leaveTimeRepository;

    private final EmployeeRepository employeeRepository;

    public LeaveTimeService(LeaveTimeRepository leaveTimeRepository,
                            EmployeeRepository employeeRepository) {
        this.leaveTimeRepository = leaveTimeRepository;
        this.employeeRepository = employeeRepository;
    }

    public void save(LeaveTimeEntity leaveTime) {
        leaveTimeRepository.save(leaveTime);
    }

    public List<LeaveTimeEntity> getLeaveRequests(String name) {
        EmployeeEntity current = employeeRepository.findByName(name);
        List<LeaveTimeEntity> leaveTimeList = leaveTimeRepository.findAllByEmployeeId(current.getId());
        return leaveTimeList.stream().filter(lt -> lt.getDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());
    }
}
