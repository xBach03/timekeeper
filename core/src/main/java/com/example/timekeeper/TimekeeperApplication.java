package com.example.timekeeper;

import com.example.timekeeper.constant.Status;
import com.example.timekeeper.entity.*;
import com.example.timekeeper.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootApplication
public class TimekeeperApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimekeeperApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AttendanceService attendanceService,
											   DepartmentService departmentService,
											   EmployeeService employeeService,
											   LeaveTimeService leaveTimeService,
											   ManagerService managerService,
											   OvertimeService overtimeService,
											   PayrollService payrollService) {
		return args -> {
			String[] title = new String[]{"Software Engineer", "Principal Engineer", "Software QA Engineer"};
			String[] gender = new String[]{"male", "female"};
			ManagerEntity manager1 = new ManagerEntity();
			manager1.setTitle("Manager, Software Development")
					.setTeamSize((short) 10);
			ManagerEntity manager2 = new ManagerEntity();
			manager2.setTitle("Manager, CSE")
					.setTeamSize((short) 10);
			DepartmentEntity department1 = new DepartmentEntity();
			department1.setName("Product R&D")
					.setField("Software Development")
					.setManager(manager1);
			DepartmentEntity department2 = new DepartmentEntity();
			department2.setName("Customer Service & Engagement")
					.setField("Software Engineering")
					.setManager(manager2);
			managerService.save(manager1);
			managerService.save(manager2);
			departmentService.save(department1);
			departmentService.save(department2);
			manager1.setDepartment(department1);
			manager2.setDepartment(department2);
			managerService.save(manager1);
			managerService.save(manager2);
			for (int i = 0; i < 20; i++) {
				EmployeeEntity employee = new EmployeeEntity();
				employee.setName("employee " + i)
						.setTitle(title[i % 3])
						.setAge((short) (20 + i))
						.setGender(gender[i % 2])
						.setAddress("hanoi")
						.setEmail("employee" + i + "@email.com")
						.setPhoneNumber("09393130" + i);
				if (i < 10) {
					employee.setDepartment(department1)
							.setManager(manager1);
				} else {
					employee.setDepartment(department2)
							.setManager(manager2);
				}
				employeeService.save(employee);
				OvertimeEntity overtime = new OvertimeEntity();
				overtime.setApproval(Status.APPROVED)
						.setEmployee(employee)
						.setDate(LocalDate.now())
						.setProject("Project" + i)
						.setTotalHour((short) i);
				overtimeService.save(overtime);
				AttendanceEntity attendance = new AttendanceEntity();
				attendance.setEmployee(employee)
						.setDate(LocalDate.now())
						.setCreated(LocalDateTime.now())
						.setUpdated(LocalDateTime.now())
						.setCheckIn(LocalTime.of(8, 30))
						.setCheckOut(LocalTime.of(17, 30));
				attendanceService.save(attendance);
				PayrollEntity payroll = new PayrollEntity();
				payroll.setEmployee(employee)
						.setBonus(1000L)
						.setTax(150L)
						.setPeriod("25.2")
						.setTotalSalary(850L);
				payrollService.save(payroll);
			}
		};
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:3000")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*");
			}
		};
	}
}
