package com.adobe.prj.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adobe.prj.entity.Student;

@RestController
@RequestMapping("api/students")
public class StudentController {

    public static final List<Student> STUDENTS = Arrays.asList(
      new Student(1, "Wonder Woman"),
      new Student(2, "Harry Potter"),
      new Student(3, "Iron Man")
    );

    @GetMapping("/{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId) {
        return STUDENTS.stream()
                .filter(student -> studentId.equals(student.getStudentId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Student " + studentId + " does not exists"
                ));
    }
}
