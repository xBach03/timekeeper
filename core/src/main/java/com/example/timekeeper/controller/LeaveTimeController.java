package com.example.timekeeper.controller;

import com.example.timekeeper.dto.LeaveReqDto;
import com.example.timekeeper.entity.LeaveTimeEntity;
import com.example.timekeeper.service.LeaveTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leave")
@Slf4j
public class LeaveTimeController {
    private final LeaveTimeService leaveTimeService;

    public LeaveTimeController(LeaveTimeService leaveTimeService) {
        this.leaveTimeService = leaveTimeService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestLeave(@RequestBody LeaveReqDto leaveReqDto, @RequestParam String name) {
        log.info("Saving leave record to db");
        leaveTimeService.save(leaveReqDto, name);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<String>> upcomingLeave(@RequestParam String name) {
        List<LeaveTimeEntity> leaveTimeList = leaveTimeService.getMonthlyDayoffs(name);

        List<String> dates = leaveTimeList.stream()
                .map(entity -> entity.getDate().toString())
                .collect(Collectors.toList());
        log.info("Leaves: {}", dates);
        return ResponseEntity.ok(dates);
    }
}
