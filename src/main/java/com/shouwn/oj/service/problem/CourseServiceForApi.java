package com.shouwn.oj.service.problem;

import java.util.List;
import java.util.Optional;

import com.shouwn.oj.exception.AlreadyExistException;
import com.shouwn.oj.exception.IllegalStateException;
import com.shouwn.oj.model.entity.member.Student;
import com.shouwn.oj.model.entity.problem.Course;
import com.shouwn.oj.service.member.StudentService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseServiceForApi {

	private CourseService courseService;

	private StudentService studentService;

	public CourseServiceForApi(CourseService courseService, StudentService studentService) {
		this.courseService = courseService;
		this.studentService = studentService;
	}

	public List<Course> getRegisteredCourses(Long studentId) {
		return studentService.findById(studentId).get().getCourses();
	}

	public List<Course> getAllCourses() {
		return courseService.findAll();
	}

	@Transactional
	public Student registerCourse(Long studentId, Long courseId) {
		Student student = studentService.findById(studentId).get();
		Optional<Course> course = courseService.findById(courseId);

		if (!course.isPresent()) {
			throw new IllegalStateException("해당 강좌가 존재하지 않습니다.");
		}

		if (!course.get().getEnabled()) {
			throw new IllegalStateException("해당 강좌는 비활성화된 강좌입니다.");
		}

		List<Course> courses = student.getCourses();

		if (courses.contains(course.get())) {
			throw new AlreadyExistException("이미 수강 중인 강좌입니다.");
		}

		courses.add(course.get());

		return studentService.save(student);
	}
}
