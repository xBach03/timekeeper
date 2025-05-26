package com.example.timekeeper.controller;

import com.example.timekeeper.constant.Status;
import com.example.timekeeper.dto.*;
import com.example.timekeeper.entity.EmployeeEntity;
import com.example.timekeeper.entity.EmployeeSessionEntity;
import com.example.timekeeper.service.AttendanceService;
import com.example.timekeeper.service.EmployeeService;
import com.example.timekeeper.service.EmployeeSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/employee")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeSessionService employeeSessionService;
    private final AttendanceService attendanceService;

    public EmployeeController(EmployeeService employeeService,
                              EmployeeSessionService employeeSessionService, AttendanceService attendanceService) {
        this.employeeService = employeeService;
        this.employeeSessionService = employeeSessionService;
        this.attendanceService = attendanceService;
    }

    @PostMapping("/register")
    public ResponseEntity<EmployeeResDto> register(@RequestBody EmployeeReqDto employeeReq) {
        EmployeeResDto savedDto = employeeService.save(new EmployeeEntity()
                .setName(employeeReq.getName())
                .setEmail(employeeReq.getEmail())
                .setDateOfBirth(employeeReq.getDateOfBirth())
                .setTitle(employeeReq.getTitle()));
        employeeSessionService.save(new EmployeeSessionEntity()
                .setEmployeeName(employeeReq.getName())
                .setStatus(Status.INACTIVE));
        if (Objects.isNull(savedDto)) {
            log.error("Error while saving employee data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("Employee saved successfully");
        return new ResponseEntity<>(savedDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody RecognizeReqDto loginReq) {
        return new ResponseEntity<>(employeeSessionService.login(loginReq.getName()), HttpStatus.OK);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashBoardResDto> navigateToIndex(@RequestParam String name) {
        DashBoardResDto dashBoardResDto = new DashBoardResDto();
        dashBoardResDto.setBreakTime("1 hour")
                .setShift(attendanceService.calculateCurrentShift(name))
                .setLeaveRequests(attendanceService.calculatePendingApprovals(name) + " pending approval(s)")
                .setNotifications(List.of("New policy update", "Shift change approved"));
        return new ResponseEntity<>(dashBoardResDto, HttpStatus.OK);
    }
}
