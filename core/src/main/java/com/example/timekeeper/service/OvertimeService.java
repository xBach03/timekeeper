package com.example.timekeeper.service;

import com.example.timekeeper.constant.Status;
import com.example.timekeeper.dto.OTLogDto;
import com.example.timekeeper.dto.OvertimeDto;
import com.example.timekeeper.entity.EmployeeEntity;
import com.example.timekeeper.entity.OvertimeEntity;
import com.example.timekeeper.repository.EmployeeRepository;
import com.example.timekeeper.repository.OvertimeRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OvertimeService {
    private final OvertimeRepository overtimeRepository;

    private final EmployeeRepository employeeRepository;

    public OvertimeService(OvertimeRepository overtimeRepository,
                           EmployeeRepository employeeRepository) {
        this.overtimeRepository = overtimeRepository;
        this.employeeRepository = employeeRepository;
    }

    public void save(OvertimeEntity overtime) {
        overtimeRepository.save(overtime);
    }

    public List<OvertimeDto> getOvertimeByEmployeeName(String name) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
        List<OvertimeEntity> entities = overtimeRepository
                .findByEmployeeNameAndDate(name, start, end);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return entities.stream().map(entity ->
             new OvertimeDto().setDate(entity.getDate())
                    .setStartHour(entity.getStartHour().format(formatter))
                    .setEndHour(entity.getEndHour().format(formatter))
                    .setTotalHour(entity.getTotalHour())
                    .setProject(entity.getProject())
                    .setApproval(entity.getApproval())
        ).toList();
    }

    public String save(OTLogDto otLogDto) {
        EmployeeEntity employee = employeeRepository.findByName(otLogDto.getName());
        OvertimeEntity overtime = new OvertimeEntity()
                .setApproval(Status.PENDING)
                .setProject(otLogDto.getProject())
                .setDate(otLogDto.getDate())
                .setEmployee(employee)
                .setStartHour(otLogDto.getStartHour())
                .setEndHour(otLogDto.getEndHour())
                .setTotalHour(
                        (float) Duration.between(otLogDto.getStartHour(), otLogDto.getEndHour()).toMinutes() / 60
                );
        overtimeRepository.save(overtime);
        return "Success";
    }
}
