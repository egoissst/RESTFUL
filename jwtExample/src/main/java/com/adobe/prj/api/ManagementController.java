package com.adobe.prj.api;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adobe.prj.entity.Student;

@RestController
@RequestMapping("api/managment")
public class ManagementController {
	 @GetMapping
	    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
	    public List<Student> getAllStudents() {
	        System.out.println("getAllStudents");
	        return StudentController.STUDENTS;
	    }

	    @PostMapping
	    @PreAuthorize("hasAuthority('student:write')")
	    public void registerNewStudent(@RequestBody Student student) {
	        System.out.println("registerNewStudent");
	        System.out.println(student);
	    }

	    @DeleteMapping("/{studentId}")
	    @PreAuthorize("hasAuthority('student:write')")
	    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
	        System.out.println("deleteStudent");
	        System.out.println(studentId);
	    }

	    @PutMapping("/{studentId}")
	    @PreAuthorize("hasAuthority('student:write')")
	    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
	        System.out.println("updateStudent");
	        System.out.println(String.format("%s %s", studentId, student));
	    }
}
