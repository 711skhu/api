package com.shouwn.oj.service.problem;

import java.util.List;

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
		Student student = studentService.findById(studentId).orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));
		return student.getCourses();
	}

	public List<Course> getCoursesByEnabled() {
		return courseService.findCoursesByEnabled();
	}

	@Transactional
	public Student registerCourse(Long studentId, Long courseId) {
		Student student = studentService.findById(studentId).orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));
		Course course = courseService.findById(courseId).orElseThrow(() -> new IllegalStateException("해당 강좌가 존재하지 않습니다."));

		if (!course.getEnabled()) {
			throw new IllegalStateException("해당 강좌는 비활성화된 강좌입니다.");
		}

		List<Course> courses = student.getCourses();

		if (courses.contains(course)) {
			throw new AlreadyExistException("이미 수강 중인 강좌입니다.");
		}

		courses.add(course);

		List<Student> students = course.getStudents();
		students.add(student);

		return studentService.save(student);
	}

	public Course courseInformation(Long courseId) {
		Course course = courseService.findById(courseId).orElseThrow(() -> new IllegalStateException("해당 강좌가 존재하지 않습니다."));
		return course;
	}
}
