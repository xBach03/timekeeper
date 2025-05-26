package com.example.timekeeper.controller;

import com.example.timekeeper.dto.AttendanceResDto;
import com.example.timekeeper.dto.TimeCheckDto;
import com.example.timekeeper.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
