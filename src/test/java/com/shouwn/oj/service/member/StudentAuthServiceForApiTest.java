package com.shouwn.oj.service.member;

import java.util.Optional;

import com.shouwn.oj.exception.AuthenticationFailedException;
import com.shouwn.oj.exception.NotFoundException;
import com.shouwn.oj.model.entity.member.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentAuthServiceForApiTest {

	@Mock
	private StudentService studentService;

	@Mock
	private StudentAuthService studentAuthService;

	@InjectMocks
	private StudentAuthServiceForApi studentAuthServiceForApi;

	private Student student;

	@BeforeEach
	void init() {
		this.student = Student.builder()
				.name("test")
				.username("test")
				.password("test12345")
				.email("test@skhu.ac.kr")
				.build();
	}

	@Test
	void loginSuccess() {
		when(studentService.findByUsername(any())).thenReturn(Optional.of(this.student));
		when(studentAuthService.isCorrectPassword(any(), any())).thenReturn(true);

		studentAuthServiceForApi.login(this.student.getUsername(), this.student.getPassword());

		verify(studentService).findByUsername(this.student.getUsername());
		verify(studentAuthService).isCorrectPassword(this.student, this.student.getPassword());
	}

	@Test
	void loginThrowsNotFoundExceptionByUsername() {
		when(studentService.findByUsername(any())).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class,
				() -> studentAuthServiceForApi.login(this.student.getUsername(), this.student.getPassword()));

		verify(studentService).findByUsername(this.student.getUsername());
		verify(studentAuthService, times(0)).isCorrectPassword(any(), any());
	}

	@Test
	void loginThrowAuthenticationFailedExceptionPassword() {
		when(studentService.findByUsername(any())).thenReturn(Optional.of(this.student));
		when(studentAuthService.isCorrectPassword(any(), any())).thenReturn(false);

		String wrongPassword = "12345test";

		assertThrows(AuthenticationFailedException.class,
				() -> studentAuthServiceForApi.login(this.student.getUsername(), wrongPassword)
		);

		verify(studentService).findByUsername(this.student.getUsername());
		verify(studentAuthService).isCorrectPassword(this.student, wrongPassword);
	}
}
