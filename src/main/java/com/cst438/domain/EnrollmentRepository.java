package com.cst438.domain;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EnrollmentRepository extends CrudRepository <Enrollment, Integer> {
	 
	@Query("select e from Enrollment e where e.student.email=:email and e.year=:year and e.semester=:semester")
	public List<Enrollment> findStudentSchedule(
			@Param("email") String email, 
			@Param("year") int year, 
			@Param("semester") String semester);
	
	@Query("select e from Enrollment e where e.student.email=:email and e.course.course_id=:course_id")
	Enrollment findByEmailAndCourseId(@Param("email") String email, @Param("course_id") int course_id);
	
	@SuppressWarnings("unchecked")
	Enrollment save(Enrollment e);
	
	@Query("delete from Enrollment where student_id=:student_id")
	@Modifying
	@Transactional
	void deleteAllByStudentId(@Param("student_id") int student_id);
}