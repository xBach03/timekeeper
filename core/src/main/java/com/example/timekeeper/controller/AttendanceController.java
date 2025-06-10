package com.example.timekeeper.controller;

import com.example.timekeeper.dto.AttendanceResDto;
import com.example.timekeeper.dto.ShiftDto;
import com.example.timekeeper.dto.TimeCheckDto;
import com.example.timekeeper.entity.AttendanceEntity;
import com.example.timekeeper.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/checkin")
    public ResponseEntity<AttendanceResDto> checkIn(@RequestBody TimeCheckDto checkInDto) {
        log.info("Employee checked in at: {}", checkInDto.getTime());
        AttendanceResDto attendanceResDto = attendanceService.checkIn(checkInDto);
        return ResponseEntity.ok(attendanceResDto);
    }

    @PostMapping("/checkout")
    public ResponseEntity<AttendanceResDto> checkout(@RequestBody TimeCheckDto checkInDto) {
        log.info("Employee checked out at: {}", checkInDto.getTime());
        AttendanceResDto attendanceResDto = attendanceService.checkOut(checkInDto);
        return ResponseEntity.ok(attendanceResDto);
    }

    @GetMapping("/shifts")
    public ResponseEntity<List<ShiftDto>> calcShifts(@RequestParam String name) {
        log.info("Getting shifts for {}", name);
        log.info("Shifts {}", attendanceService.getWeeklyShifts(name));
        return ResponseEntity.ok(attendanceService.getWeeklyShifts(name));
    }

    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> getTodayAttendance(@RequestParam String name) {
        AttendanceEntity attendance = attendanceService.getTodayAttendance(name);
        if (attendance == null) {
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.NOT_FOUND);
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Map<String, Object> result = new HashMap<>();
        result.put("date", attendance.getDate() != null ? attendance.getDate().toString() : null);
        result.put("checkIn", attendance.getCheckIn() != null ? attendance.getCheckIn().format(timeFormatter) : null);
        result.put("checkOut", attendance.getCheckOut() != null ? attendance.getCheckOut().format(timeFormatter) : null);

        return ResponseEntity.ok(result);
    }


}
