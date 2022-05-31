package com.cst438;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

//End to end testing for the add student endpoint

@SpringBootTest
public class EndToEndAddStudentTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = "C:\\Users\\Delaney\\Downloads\\chromedriver.exe";

	public static final String URL = "https://cst438-register-front.herokuapp.com/addstudent/";
	
	public static final String TEST_STUDENT_NAME = "Test Student";

	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";

	public static final int TEST_STATUS_CODE = 0;

	public static final int SLEEP_DURATION = 1000; // 1 second.

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Test
	public void addStudentTest() throws Exception {
		
		//Check if test student exists
		Student s = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
		if (s != null)
		{
			//Remove all enrollments for student
			enrollmentRepository.deleteAllByStudentId(s.getStudent_id());
			//Delete test student from repository
			studentRepository.delete(s);
		}

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			//Enter test student name and email
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_STUDENT_NAME);
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_STUDENT_EMAIL);
			
			//Choose select value
			driver.findElement(By.xpath("//input[@name='status_code']")).sendKeys("value", Integer.toString(TEST_STATUS_CODE));

			// Locate and click "Add Student" button
			// There is only one button, if more are added this needs to be changed since findElement returns only one element, null, or an error
			driver.findElement(By.xpath("//button")).click();
			Thread.sleep(SLEEP_DURATION);

			// verify that student was inserted into database
			Student student = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			assertNotNull(student, "Added student not found in database");

		} catch (Exception ex) {
			throw ex;
		} finally {

			// clean up database.
			Student student = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			if (student != null)
				studentRepository.delete(student);

			driver.quit();
		}

	}
}
