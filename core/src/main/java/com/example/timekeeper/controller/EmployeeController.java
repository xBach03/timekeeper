package com.example.timekeeper.controller;

import com.example.timekeeper.dto.EmployeeReqDto;
import com.example.timekeeper.dto.EmployeeResDto;
import com.example.timekeeper.entity.EmployeeEntity;
import com.example.timekeeper.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("api/employee")
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/register")
    public ResponseEntity<EmployeeResDto> register(@RequestBody EmployeeReqDto employeeReq) {
        EmployeeResDto savedDto = employeeService.save(new EmployeeEntity()
                .setName(employeeReq.getName())
                .setEmail(employeeReq.getEmail())
                .setDateOfBirth(employeeReq.getDateOfBirth())
                .setTitle(employeeReq.getTitle()));
        if (Objects.isNull(savedDto)) {
            log.error("Error while saving employee data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("Employee saved successfully");
        return new ResponseEntity<>(savedDto, HttpStatus.OK);
    }
}
