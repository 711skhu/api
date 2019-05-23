package com.shouwn.oj.service.member;

import com.shouwn.oj.exception.member.PasswordIncorrectException;
import com.shouwn.oj.exception.member.UsernameNotExistException;
import com.shouwn.oj.model.entity.member.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class StudentServiceForApiTest {

	@Mock
	private StudentService studentService;

	private StudentServiceForApi studentServiceForApi;

	private Student student;

	@BeforeEach
	void init() {
		this.studentServiceForApi = new StudentServiceForApi(this.studentService);
		this.student = Student.builder()
				.name("test")
				.username("test")
				.password("test12345")
				.email("test@skhu.ac.kr")
				.build();
	}

	@Test
	public void loginThrowsUsernameNotExistException() {
		when(studentService.findByUsername(any())).thenThrow(UsernameNotExistException.class);

		assertThrows(UsernameNotExistException.class,
				() -> studentServiceForApi.login(this.student.getUsername(), this.student.getPassword()));

		verify(studentService).findByUsername(this.student.getUsername());
	}

	@Test
	public void loginThrowPasswordIncorrectException() {
		when(studentService.findByUsername(any())).thenReturn(this.student);
		when(studentService.isCorrectPassword(any(),any())).thenReturn(false);

		String wrongPassword = "12345test";

		assertThrows(PasswordIncorrectException.class,
				() -> studentServiceForApi.login(this.student.getUsername(), wrongPassword)
		);

		verify(studentService).findByUsername(this.student.getUsername());
		verify(studentService).isCorrectPassword(this.student, wrongPassword);
	}
}
