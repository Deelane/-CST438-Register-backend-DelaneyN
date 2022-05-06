package com.cst438.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@RestController
public class StudentController {
	
	@Autowired
	StudentRepository studentRepository;

	@PostMapping("/student/insert")
	@Transactional
	public void insertStudent( @RequestBody Student student) 
	{
		//Do not allow students with duplicate emails
		if (studentRepository.findByEmail(student.getEmail()) == null) 
		{
			studentRepository.save(student);
		} 
		else 
		{
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student with given e-mail already exists.");
		}
	}
	
	@PutMapping("/student/{student_id}/addHold")
	@Transactional
	public void addStudentHold(@PathVariable int student_id)
	{
		Student student = studentRepository.findById(student_id).orElse(null);
		if (student != null) 
		{
			studentRepository.addHold(student_id);
		} 
		else 
		{
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student with given id does not exist.");
		}
	}
	
	@PutMapping("/student/{student_id}/releaseHold")
	@Transactional
	public void releaseStudentHold(@PathVariable int student_id)
	{
		Student student = studentRepository.findById(student_id).orElse(null);
		if (student != null) 
		{
			studentRepository.releaseHold(student_id);
		} 
		else 
		{
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student with given id does not exist.");
		}
	}
	
}
