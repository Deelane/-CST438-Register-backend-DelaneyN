package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cst438.domain.CourseDTOG;
import com.cst438.domain.CourseDTOG.GradeDTO;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

@RestController
public class CourseController {
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	/*
	 * endpoint used by gradebook service to transfer final course grades
	 */
	@PutMapping("/course/{course_id}")
	@Transactional
	public void updateCourseGrades( @RequestBody CourseDTOG courseDTOG, @PathVariable("course_id") int course_id) {
		
		//Update grades one by one for each student in course
		for (GradeDTO gradeDTO: courseDTOG.grades)
		{
			//find enrollment for student
			Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(gradeDTO.student_email, course_id);
			//update grade and save to database
			enrollment.setCourseGrade(gradeDTO.grade);
			enrollmentRepository.save(enrollment);
		}
	}

}
