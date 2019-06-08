package com.shouwn.oj.controller.problem;

import java.util.List;
import java.util.stream.Collectors;

import com.shouwn.oj.model.entity.member.Student;
import com.shouwn.oj.model.entity.problem.Course;
import com.shouwn.oj.model.response.ApiResponse;
import com.shouwn.oj.model.response.CommonResponse;
import com.shouwn.oj.model.response.CourseInfoResponse;
import com.shouwn.oj.model.response.CourseListResponse;
import com.shouwn.oj.service.problem.CourseServiceForApi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("isAuthenticated()")
public class CourseController {

	private CourseServiceForApi courseServiceForApi;

	public CourseController(CourseServiceForApi courseServiceForApi) {
		this.courseServiceForApi = courseServiceForApi;
	}

	@GetMapping("/own/courses")
	public ApiResponse<?> getRegisteredCourses(@RequestAttribute Long requesterId) {
		List<Course> courses = courseServiceForApi.getRegisteredCourses(requesterId);
		List<CourseListResponse> courseListResponses = courses.stream()
				.map(CourseListResponse::new)
				.collect(Collectors.toList());

		return CommonResponse.builder()
				.status(HttpStatus.OK)
				.message("수강중인 강좌 목록조회 성공")
				.data(courseListResponses)
				.build();
	}

	@GetMapping("/courses/enabled")
	public ApiResponse<?> getCoursesByEnabled() {
		List<Course> courses = courseServiceForApi.getCoursesByEnabled();
		List<CourseListResponse> courseListResponses = courses.stream()
				.map(CourseListResponse::new)
				.collect(Collectors.toList());

		return CommonResponse.builder()
				.status(HttpStatus.OK)
				.message("강좌 목록조회 성공")
				.data(courseListResponses)
				.build();
	}

	@PostMapping("register/courses/{courseId}")
	public ApiResponse<?> registerCourse(@RequestAttribute Long requesterId, @PathVariable("courseId") Long courseId) {
		Student student = courseServiceForApi.registerCourse(requesterId, courseId);

		return CommonResponse.builder()
				.status(HttpStatus.CREATED)
				.message("강좌 수강신청 성공")
				.build();
	}

	@GetMapping("/courses/{courseId}")
	public ApiResponse<?> getCourse(@PathVariable("courseId") Long courseId) {
		Course course = courseServiceForApi.courseInformation(courseId);

		CourseInfoResponse courseInfoResponse = CourseInfoResponse.builder()
				.courseId(course.getId())
				.courseName(course.getName())
				.description(course.getDescription())
				.build();

		return CommonResponse.builder()
				.status(HttpStatus.OK)
				.message("강좌소개 조회 성공")
				.data(courseInfoResponse)
				.build();
	}

}
