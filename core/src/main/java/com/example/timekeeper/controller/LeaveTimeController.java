package com.example.timekeeper.controller;

import com.example.timekeeper.dto.LeaveReqDto;
import com.example.timekeeper.entity.LeaveTimeEntity;
import com.example.timekeeper.service.LeaveTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<List<Map<String, Object>>> upcomingLeave(@RequestParam String name) {
        List<LeaveTimeEntity> leaveTimeList = leaveTimeService.getMonthlyDayoffs(name);

        List<Map<String, Object>> result = leaveTimeList.stream().map(entity -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", entity.getDate().toString());

            boolean fullDay = leaveTimeService.isFullDay(entity.getStartHour(), entity.getEndHour());
            map.put("fullDay", fullDay);

            String hours = entity.getStartHour().toString() + " - " + entity.getEndHour().toString();
            map.put("hours", hours);
            map.put("hoursNum", Duration.between(entity.getStartHour(), entity.getEndHour()).toMinutes() / 60);
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }



}
