package com.shouwn.oj.service.member;

import java.util.Optional;

import com.shouwn.oj.exception.AuthenticationFailedException;
import com.shouwn.oj.exception.NotFoundException;
import com.shouwn.oj.model.entity.member.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StudentServiceForApiTest {

	private StudentService studentService;

	private StudentServiceForApi studentServiceForApi;

	private Student student;

	@BeforeEach
	void init() {
		studentService = mock(StudentService.class);
		this.studentServiceForApi = new StudentServiceForApi(this.studentService);
		this.student = Student.builder()
				.name("test")
				.username("test")
				.password("test12345")
				.email("test@skhu.ac.kr")
				.build();
	}

	@Test
	public void loginSuccess() {
		when(studentService.findByUsername(any())).thenReturn(Optional.of(this.student));
		when(studentService.isCorrectPassword(any(), any())).thenReturn(true);

		studentServiceForApi.login(this.student.getUsername(), this.student.getPassword());

		verify(studentService).findByUsername(this.student.getUsername());
		verify(studentService).isCorrectPassword(this.student, this.student.getPassword());
	}

	@Test
	public void loginThrowsNotFoundExceptionByUsername() {
		when(studentService.findByUsername(any())).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class,
				() -> studentServiceForApi.login(this.student.getUsername(), this.student.getPassword()));

		verify(studentService).findByUsername(this.student.getUsername());
	}

	@Test
	public void loginThrowAuthenticationFailedExceptionPassword() {
		when(studentService.findByUsername(any())).thenReturn(Optional.of(this.student));
		when(studentService.isCorrectPassword(any(), any())).thenReturn(false);

		String wrongPassword = "12345test";

		assertThrows(AuthenticationFailedException.class,
				() -> studentServiceForApi.login(this.student.getUsername(), wrongPassword)
		);

		verify(studentService).findByUsername(this.student.getUsername());
		verify(studentService).isCorrectPassword(this.student, wrongPassword);
	}
}
