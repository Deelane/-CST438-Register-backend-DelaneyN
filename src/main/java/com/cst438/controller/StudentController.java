package com.cst438.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Admin;
import com.cst438.domain.AdminRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://cst438-register-front.herokuapp.com/"})
public class StudentController {
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired AdminRepository adminRepository;
	
	@PostMapping("/student/insert")
	@Transactional
	public void insertStudent( @RequestBody Student student, @AuthenticationPrincipal OAuth2User principal) 
	{
		//retrieve email from login
		String admin_email = principal.getAttribute("email");
		
		//check admin database for admin status
		Admin admin = adminRepository.findByEmail(admin_email);
		if (admin != null)
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
		else
		{
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Operation can only be performed by administrators.");
		}
	}
	
	@PutMapping("/student/{student_id}/addHold")
	@Transactional
	public void addStudentHold(@PathVariable int student_id, @AuthenticationPrincipal OAuth2User principal)
	{
		//retrieve email from login
		String admin_email = principal.getAttribute("email");
		
		//check admin database for admin status
		Admin admin = adminRepository.findByEmail(admin_email);
		if (admin != null)
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
		else
		{
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Operation can only be performed by administrators.");
		}
	}
	
	@PutMapping("/student/{student_id}/releaseHold")
	@Transactional
	public void releaseStudentHold(@PathVariable int student_id, @AuthenticationPrincipal OAuth2User principal)
	{
		//retrieve email from login
		String admin_email = principal.getAttribute("email");
		
		//check admin database for admin status
		Admin admin = adminRepository.findByEmail(admin_email);
		if (admin != null)
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
		else
		{
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Operation can only be performed by administrators.");
		}
	}
	
}
