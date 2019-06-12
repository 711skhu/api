package com.shouwn.oj.service.member;

import com.shouwn.oj.exception.AlreadyExistException;
import com.shouwn.oj.model.entity.member.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceForApiTest {

	@Mock
	private StudentService studentService;

	@InjectMocks
	private StudentServiceForApi studentServiceForApi;

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
	void makeStudentSuccess() {
		when(studentService.isRegisteredUsername(any())).thenReturn(false);
		when(studentService.isRegisteredEmail(any())).thenReturn(false);

		studentServiceForApi.makeStudent(this.student.getName(),
				this.student.getUsername(),
				this.student.getPassword(),
				this.student.getEmail());

		final ArgumentCaptor<Student> saveCaptor = ArgumentCaptor.forClass(Student.class);

		verify(studentService).save(saveCaptor.capture());

		assertEquals(this.student.getUsername(), saveCaptor.getValue().getUsername());
		assertEquals(this.student.getPassword(), saveCaptor.getValue().getPassword());
	}

	@Test
	void makeStudentThrowsAlreadyExistExceptionByUsername() {
		when(studentService.isRegisteredUsername(any())).thenReturn(true);

		assertThrows(AlreadyExistException.class,
				() -> studentServiceForApi.makeStudent(this.student.getName(),
						this.student.getUsername(),
						this.student.getPassword(),
						this.student.getEmail())
		);

		verify(studentService).isRegisteredUsername(this.student.getUsername());
		verify(studentService, times(0)).isRegisteredEmail(this.student.getEmail());
	}

	@Test
	void makeStudentThrowsAlreadyExistExceptionByEmail() {
		when(studentService.isRegisteredUsername(any())).thenReturn(false);
		when(studentService.isRegisteredEmail(any())).thenReturn(true);

		assertThrows(AlreadyExistException.class,
				() -> studentServiceForApi.makeStudent(this.student.getName(),
						this.student.getUsername(),
						this.student.getPassword(),
						this.student.getEmail())
		);

		verify(studentService).isRegisteredUsername(this.student.getUsername());
		verify(studentService).isRegisteredEmail(this.student.getEmail());
	}
}