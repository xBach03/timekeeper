package com.example.timekeeper.service;

import com.example.timekeeper.constant.Status;
import com.example.timekeeper.dto.LeaveReqDto;
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

    public void save(LeaveReqDto leaveReqDto, String name) {
        EmployeeEntity current = employeeRepository.findByName(name);
        leaveTimeRepository.save(
                new LeaveTimeEntity()
                        .setEmployee(current)
                        .setDate(leaveReqDto.getDate())
                        .setStartHour(leaveReqDto.getStartHour())
                        .setEndHour(leaveReqDto.getEndHour())
                        .setStatus(Status.PENDING)
                        .setCategory(leaveReqDto.getCategory())
        );
    }

    public List<LeaveTimeEntity> getMonthlyDayoffs(String name) {
        List<LeaveTimeEntity> leaveTimeList = leaveTimeRepository.findAllByEmployeeName(name);
        return leaveTimeList.stream()
                .filter(lt -> lt.getDate().getMonth().equals(LocalDate.now().getMonth()))
                .filter(lt -> lt.getStatus().equalsIgnoreCase(Status.APPROVED))
                .collect(Collectors.toList());
    }
}
