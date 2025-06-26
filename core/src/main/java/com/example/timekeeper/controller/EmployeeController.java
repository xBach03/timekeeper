package com.example.timekeeper.controller;

import com.example.timekeeper.constant.Status;
import com.example.timekeeper.dto.*;
import com.example.timekeeper.entity.EmployeeEntity;
import com.example.timekeeper.entity.EmployeeSessionEntity;
import com.example.timekeeper.service.*;
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

    private final PayrollService payrollService;

    private final OvertimeService overtimeService;

    public EmployeeController(EmployeeService employeeService,
                              EmployeeSessionService employeeSessionService,
                              AttendanceService attendanceService,
                              PayrollService payrollService,
                              OvertimeService overtimeService) {
        this.employeeService = employeeService;
        this.employeeSessionService = employeeSessionService;
        this.attendanceService = attendanceService;
        this.payrollService = payrollService;
        this.overtimeService = overtimeService;
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
        var result = employeeSessionService.login(loginReq.getName());
        if (Objects.nonNull(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String name) {
        return new ResponseEntity<>(employeeSessionService.logout(name), HttpStatus.OK);
    }

    @GetMapping("/payroll")
    public ResponseEntity<List<PayrollDto>> getPayrollList(@RequestParam String name) {
        List<PayrollDto> result = payrollService.getPayroll(name);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/overtime")
    public ResponseEntity<List<OvertimeDto>> getOverTimeList(@RequestParam String name) {
        List<OvertimeDto> result = overtimeService.getOvertimeByEmployeeName(name);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("overtime_log")
    public ResponseEntity<String> logOverTime(@RequestBody OTLogDto otLogDto) {
        return new ResponseEntity<>(overtimeService.save(otLogDto), HttpStatus.OK);
    }
}
