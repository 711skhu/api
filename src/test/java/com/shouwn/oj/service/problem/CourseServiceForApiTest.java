package com.shouwn.oj.service.problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.shouwn.oj.exception.AlreadyExistException;
import com.shouwn.oj.exception.IllegalStateException;
import com.shouwn.oj.model.entity.member.Student;
import com.shouwn.oj.model.entity.problem.Course;
import com.shouwn.oj.service.member.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CourseServiceForApiTest {

	private CourseService courseService;

	private StudentService studentService;

	private CourseServiceForApi courseServiceForApi;

	private Student student;

	private Course registeredCourse;

	private Course notRegisteredAndEnabledCourse;

	private Course notRegisteredAndDisabledCourse;

	@BeforeEach
	void init() {
		this.courseService = mock(CourseService.class);
		this.studentService = mock(StudentService.class);
		this.courseServiceForApi = new CourseServiceForApi(courseService, studentService);

		this.registeredCourse = Course.builder()
				.name("registered")
				.description("test")
				.enabled(true)
				.professor(null)
				.build();
		this.registeredCourse.setId(1L);

		this.notRegisteredAndEnabledCourse = Course.builder()
				.name("notRegisteredAndEnabledCourse")
				.description("test")
				.enabled(true)
				.professor(null)
				.build();
		this.notRegisteredAndEnabledCourse.setId(2L);

		this.notRegisteredAndDisabledCourse = Course.builder()
				.name("notRegisteredAndEnabledCourse")
				.description("test")
				.enabled(false)
				.professor(null)
				.build();
		this.notRegisteredAndEnabledCourse.setId(3L);

		this.student = Student.builder()
				.name("test")
				.username("test")
				.password("test12345")
				.email("test@skhu.ac.kr")
				.build();
		this.student.setId(1L);

		List<Course> courses = new ArrayList<>();
		courses.add(registeredCourse);

		this.student.setCourses(courses);
	}

	@Test
	public void getRegisteredCoursesSuccess() {
		when(studentService.findById(any())).thenReturn(Optional.of(student));

		List<Course> courses = courseServiceForApi.getRegisteredCourses(this.student.getId());

		verify(studentService).findById(this.student.getId());
	}

	@Test
	public void registerCourseSuccess() {
		when(courseService.findById(any())).thenReturn(Optional.of(notRegisteredAndEnabledCourse));
		when(studentService.findById(any())).thenReturn(Optional.of(this.student));

		int beforeCourseSize = this.student.getCourses().size();

		final ArgumentCaptor<Student> saveCaptor = ArgumentCaptor.forClass(Student.class);

		Long courseId = this.notRegisteredAndEnabledCourse.getId();

		courseServiceForApi.registerCourse(this.student.getId(), courseId);

		verify(courseService).findById(courseId);
		verify(studentService).findById(this.student.getId());
		verify(studentService).save(saveCaptor.capture());

		assertEquals(beforeCourseSize + 1, saveCaptor.getValue().getCourses().size());
	}

	@Test
	public void registerCourseThrowsAlreadyExistException() {
		when(courseService.findById(any())).thenReturn(Optional.of(registeredCourse));
		when(studentService.findById(any())).thenReturn(Optional.of(this.student));

		Long courseId = this.registeredCourse.getId();

		assertThrows(AlreadyExistException.class, () -> courseServiceForApi.registerCourse(this.student.getId(), courseId));

		verify(courseService).findById(courseId);
		verify(studentService).findById(this.student.getId());
	}

	@Test
	public void registerCourseThrowsIllegalStateExceptionWhenCourseIsEmpty() {
		when(courseService.findById(any())).thenReturn(Optional.empty());
		when(studentService.findById(any())).thenReturn(Optional.of(this.student));

		Long courseId = 4L;

		assertThrows(IllegalStateException.class, () -> courseServiceForApi.registerCourse(this.student.getId(), courseId));

		verify(courseService).findById(courseId);
		verify(studentService).findById(this.student.getId());
	}

	@Test
	public void registerCourseThrowsIllegalStateExceptionWhenCourseIsDisable() {
		when(courseService.findById(any())).thenReturn(Optional.of(this.notRegisteredAndDisabledCourse));
		when(studentService.findById(any())).thenReturn(Optional.of(this.student));

		Long courseId = this.notRegisteredAndDisabledCourse.getId();

		assertThrows(IllegalStateException.class, () -> courseServiceForApi.registerCourse(this.student.getId(), courseId));

		verify(courseService).findById(courseId);
		verify(studentService).findById(this.student.getId());
	}

	@Test
	public void courseInformationThrowsIllegalStateExceptionWhenWrongCourseId() {
		when(courseService.findById(any())).thenReturn(Optional.empty());

		assertThrows(IllegalStateException.class, () -> courseServiceForApi.courseInformation(this.registeredCourse.getId()));

		verify(courseService).findById(any());
	}
}
