package com.example.timekeeper.controller;

import com.example.timekeeper.dto.LeaveReqDto;
import com.example.timekeeper.service.LeaveTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
