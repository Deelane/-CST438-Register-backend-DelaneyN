package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.controller.StudentController;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class StudentControllerTest {

	public static final int TEST_STUDENT_ID = 1;
	public static final int TEST_STATUS_CODE = 0;
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME  = "test";

	@MockBean
	StudentRepository studentRepository;
	
	@MockBean
	GradebookService gradebookService;

	@Autowired
	private MockMvc mvc;
	
	@Test
	public void insertStudent() throws Exception {
		
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(TEST_STATUS_CODE);
		student.setStudent_id(TEST_STUDENT_ID);
		
		// given  -- stubs for database repositories that return test data
	    given(studentRepository.save(any(Student.class))).willReturn(student);
		
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student/insert")
			      .content(asJsonString(student))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void addStudentHold() throws Exception {
		
		MockHttpServletResponse response;
		String path = "/student/" + TEST_STUDENT_ID + "/addHold";
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(TEST_STATUS_CODE);
		student.setStudent_id(TEST_STUDENT_ID);
				
	    given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(Optional.of(student));

		response = mvc.perform(
				MockMvcRequestBuilders
			      .put(path)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());

		// verify that repository add hold method was called.
		verify(studentRepository).addHold(TEST_STUDENT_ID);
		
	}
	
	@Test
	public void releaseStudentHold() throws Exception {
		
		MockHttpServletResponse response;
		String path = "/student/" + TEST_STUDENT_ID + "/releaseHold";
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(TEST_STATUS_CODE);
		student.setStudent_id(TEST_STUDENT_ID);

		given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(Optional.of(student));

		response = mvc.perform(
				MockMvcRequestBuilders
			      .put(path)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		// verify that return status = OK (value 200) 
		assertEquals(200, response.getStatus());

		// verify that repository add hold method was called.
		verify(studentRepository).releaseHold(TEST_STUDENT_ID);

	}
	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}


