package com.adobe.prj;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/employees", method = RequestMethod.GET, produces = { "application/json", "application/xml" })

public class EmployeeController {
	@GetMapping("/{id}")
	public Employee firstPage(@PathVariable int id) {
		Employee emp = new Employee();
		emp.setName("emp1");
		emp.setDesignation("manager");
		emp.setEmpId("1");
		emp.setSalary(3000);
		return emp;
	}

	@GetMapping()
	public List<Employee> getEmployees() {
		return Arrays.asList(new Employee("1","emp1","manager",830000), new Employee("2","emp2","sales",730000));
	}
	
}
