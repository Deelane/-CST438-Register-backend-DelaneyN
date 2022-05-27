package com.cst438.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends CrudRepository <Student, Integer> {
	
	// declare the following method to return a single Student object
	// default JPA behavior that findBy methods return List<Student> except for findById.
	public Student findByEmail(String email);
	
	@Query("UPDATE Student SET status_code = 1 WHERE student_id=:student_id")
	@Modifying //required when updating values
	public void addHold(@Param("student_id") int student_id);
	
	@Query("UPDATE Student SET status_code = 0 WHERE student_id=:student_id")
	@Modifying //required when updating values
	public void releaseHold(@Param("student_id") int student_id);
}
