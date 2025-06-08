package com.example.timekeeper.service;

import com.example.timekeeper.constant.Status;
import com.example.timekeeper.dto.AttendanceResDto;
import com.example.timekeeper.dto.ShiftDto;
import com.example.timekeeper.dto.TimeCheckDto;
import com.example.timekeeper.entity.AttendanceEntity;
import com.example.timekeeper.entity.EmployeeEntity;
import com.example.timekeeper.entity.LeaveTimeEntity;
import com.example.timekeeper.repository.AttendanceRepository;
import com.example.timekeeper.repository.EmployeeRepository;
import com.example.timekeeper.repository.LeaveTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final EmployeeRepository employeeRepository;

    private final LeaveTimeRepository leaveTimeRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             EmployeeRepository employeeRepository,
                             LeaveTimeRepository leaveTimeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTimeRepository = leaveTimeRepository;
    }

    public void save(AttendanceEntity attendance) {
        attendanceRepository.save(attendance);
    }

    public AttendanceResDto checkIn(TimeCheckDto checkInDto) {
        EmployeeEntity current = employeeRepository.findByName(checkInDto.getName());
        ZonedDateTime zonedDateTime = checkInDto.getTime().atZoneSameInstant(ZoneId.systemDefault());
        AttendanceEntity timeRecord = attendanceRepository.findByEmployeeAndDate(current, zonedDateTime.toLocalDate());
        if (Objects.nonNull(timeRecord)) {
            timeRecord.setCheckIn(zonedDateTime.toLocalTime());
            attendanceRepository.save(timeRecord);
        } else {
            AttendanceEntity attendanceEntity = new AttendanceEntity()
                    .setEmployee(current)
                    .setCheckIn(zonedDateTime.toLocalTime())
                    .setDate(zonedDateTime.toLocalDate());
            AttendanceEntity saved = attendanceRepository.save(attendanceEntity);
        }
        return new AttendanceResDto()
                .setDate(zonedDateTime.toLocalDate())
                .setTime(zonedDateTime.toLocalTime())
                .setName(current.getName());
    }

    public AttendanceResDto checkOut(TimeCheckDto checkOutDto) {
        EmployeeEntity current = employeeRepository.findByName(checkOutDto.getName());
        ZonedDateTime zonedDateTime = checkOutDto.getTime().atZoneSameInstant(ZoneId.systemDefault());
        AttendanceEntity timeRecord = attendanceRepository.findByEmployeeAndDate(current, zonedDateTime.toLocalDate());
        timeRecord.setCheckOut(zonedDateTime.toLocalTime());
        attendanceRepository.save(timeRecord);
        return new AttendanceResDto()
                .setDate(timeRecord.getDate())
                .setTime(timeRecord.getCheckOut())
                .setName(current.getName());
    }

    public String calculateCurrentShift(String name) {
        EmployeeEntity employee = employeeRepository.findByName(name);
        if (employee == null) return "Unknown employee";

        // Default working hours
        LocalTime defaultStart = LocalTime.of(8, 30);
        LocalTime defaultEnd = LocalTime.of(17, 30);

        LeaveTimeEntity leave = leaveTimeRepository.findByEmployeeIdAndDate(employee.getId(), LocalDate.now());

        if (leave != null && leave.getStartHour() != null && leave.getEndHour() != null) {
            LocalTime leaveStart = leave.getStartHour();
            LocalTime leaveEnd = leave.getEndHour();

            // If leave covers the whole day
            if (!leaveStart.isAfter(defaultStart) && !leaveEnd.isBefore(defaultEnd)) {
                return "On leave today";
            }

            // Adjust working hours if leave overlaps
            if (leaveStart.isAfter(defaultStart) && leaveEnd.isBefore(defaultEnd)) {
                // Leave is in the middle: split into two shifts (for simplicity, show first half)
                return formatTime(defaultStart) + " – " + formatTime(leaveStart);
            } else if (leaveStart.isBefore(defaultStart) && leaveEnd.isBefore(defaultEnd)) {
                // Morning leave
                return formatTime(leaveEnd) + " – " + formatTime(defaultEnd);
            } else if (leaveStart.isAfter(defaultStart) && leaveEnd.isAfter(defaultEnd)) {
                // Afternoon leave
                return formatTime(defaultStart) + " – " + formatTime(leaveStart);
            }
        }

        // Default shift if no leave or no overlap
        return formatTime(defaultStart) + " – " + formatTime(defaultEnd);
    }

    private String formatTime(LocalTime time) {
        return time.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
    }

    public long calculatePendingApprovals(String name) {
        EmployeeEntity current = employeeRepository.findByName(name);
        List<LeaveTimeEntity> leaveTimeList = leaveTimeRepository.findAllByEmployeeId(current.getId());
        return leaveTimeList.stream()
                .filter(lt -> lt.getDate().isAfter(LocalDate.now()))
                .filter(lt -> lt.getStatus().equals(Status.PENDING))
                .count();
    }

    public List<AttendanceEntity> getPastWeekAttendance(String employeeName) {
        LocalDate startDate = getPreviousMonday();
        LocalDate endDate = getPreviousFriday();
        return attendanceRepository.findWeeklyAttendance(employeeName, startDate, endDate);
    }


    private LocalDate getPreviousMonday() {
        LocalDate today = LocalDate.now();
        return today.with(java.time.DayOfWeek.MONDAY).minusWeeks(1);
    }

    private LocalDate getPreviousFriday() {
        return getPreviousMonday().plusDays(4);
    }

    public String formatWithOrdinalSuffix(LocalDate date) {
        int day = date.getDayOfMonth();
        String suffix;

        if (day >= 11 && day <= 13) {
            suffix = "th";
        } else {
            switch (day % 10) {
                case 1: suffix = "st"; break;
                case 2: suffix = "nd"; break;
                case 3: suffix = "rd"; break;
                default: suffix = "th";
            }
        }

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");
        return date.getDayOfWeek().toString().substring(0, 3) + " (" + date.format(monthFormatter) + " " + day + suffix + ")";
    }

    public List<ShiftDto> getWeeklyShifts(String name) {
        LocalDate startDate = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endDate = startDate.plusDays(4);
        List<LeaveTimeEntity> leaves = leaveTimeRepository.findWeeklyLeaves(name, startDate, endDate, Status.APPROVED);
        // Group leaves by date for quick lookup
        Map<LocalDate, List<LeaveTimeEntity>> leaveMap = leaves.stream()
                .collect(Collectors.groupingBy(LeaveTimeEntity::getDate));

        List<ShiftDto> shifts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            LocalDate date = startDate.plusDays(i);
            LocalTime shiftStart = LocalTime.of(8, 30);
            LocalTime shiftEnd = LocalTime.of(17, 30);

            List<LeaveTimeEntity> dailyLeaves = leaveMap.getOrDefault(date, Collections.emptyList());

            // If leave fully covers the day, skip shift
            boolean fullyCovered = dailyLeaves.stream().anyMatch(l ->
                    !l.getStartHour().isAfter(shiftStart) && !l.getEndHour().isBefore(shiftEnd)
            );

            if (fullyCovered) {
                continue;
            }

            // Optionally adjust shift hours if partial leave (skipped here for simplicity)
            // For now, just keep default shift even if partially on leave

            shifts.add(new ShiftDto()
                    .setDate(date)
                    .setTime(String.format("%02d:%02d - %02d:%02d", shiftStart.getHour(), shiftStart.getMinute(), shiftEnd.getHour(), shiftEnd.getMinute()))
                    .setLocation("Main Office"));
        }
        log.info("Shifts: {}", shifts);

        return shifts;
    }

}
