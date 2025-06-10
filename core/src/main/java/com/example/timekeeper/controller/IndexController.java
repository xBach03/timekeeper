package com.example.timekeeper.controller;

import com.example.timekeeper.constant.Status;
import com.example.timekeeper.dto.DashBoardResDto;
import com.example.timekeeper.dto.LeaveStatusChartDto;
import com.example.timekeeper.dto.WeeklyAttendanceDto;
import com.example.timekeeper.entity.AttendanceEntity;
import com.example.timekeeper.entity.LeaveTimeEntity;
import com.example.timekeeper.service.AttendanceService;
import com.example.timekeeper.service.LeaveTimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/index")
public class IndexController {

    private final AttendanceService attendanceService;

    private final LeaveTimeService leaveTimeService;

    public IndexController(AttendanceService attendanceService,
                           LeaveTimeService leaveTimeService) {
        this.attendanceService = attendanceService;
        this.leaveTimeService = leaveTimeService;
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

    @GetMapping("/leave-status")
    public ResponseEntity<List<LeaveStatusChartDto>> getLeaveStatusChart(@RequestParam String name) {
        List<LeaveTimeEntity> requests = leaveTimeService.getLeaveRequests(name);

        long approved = requests.stream().filter(r -> r.getStatus().equalsIgnoreCase(Status.APPROVED)).count();
        long pending = requests.stream().filter(r -> r.getStatus().equalsIgnoreCase(Status.PENDING)).count();
        long rejected = requests.stream().filter(r -> r.getStatus().equalsIgnoreCase(Status.REJECTED)).count();

        List<LeaveStatusChartDto> chartData = List.of(
                new LeaveStatusChartDto("Approved", approved),
                new LeaveStatusChartDto("Pending", pending),
                new LeaveStatusChartDto("Rejected", rejected)
        );

        return ResponseEntity.ok(chartData);
    }

    @GetMapping("/weekly-attendance")
    public ResponseEntity<List<WeeklyAttendanceDto>> getWeeklyAttendance(@RequestParam String name) {
        List<AttendanceEntity> attendanceList = attendanceService.getWeeklyAttendance(name);

        List<WeeklyAttendanceDto> chartData = attendanceList.stream()
                .map(a -> new WeeklyAttendanceDto(
                        attendanceService.formatWithOrdinalSuffix(a.getDate()),
                        a.getCheckIn() != null && a.getCheckOut() != null
                                ? Duration.between(a.getCheckIn(), a.getCheckOut()).toHours()
                                : 0
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(chartData);
    }
}
