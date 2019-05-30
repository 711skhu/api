package com.shouwn.oj.controller.problem;

import java.util.ArrayList;
import java.util.List;

import com.shouwn.oj.model.entity.member.Student;
import com.shouwn.oj.model.entity.problem.Course;
import com.shouwn.oj.model.response.ApiResponse;
import com.shouwn.oj.model.response.CommonResponse;
import com.shouwn.oj.model.response.CourseListResponse;
import com.shouwn.oj.service.problem.CourseServiceForApi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("courses")
@PreAuthorize("isAuthenticated()")
public class CourseController {

	private CourseServiceForApi courseServiceForApi;

	public CourseController(CourseServiceForApi courseServiceForApi) {
		this.courseServiceForApi = courseServiceForApi;
	}

	@GetMapping
	public ApiResponse<?> getRegisteredCourses(@RequestAttribute Long requesterId) {
		List<Course> courses = courseServiceForApi.getRegisteredCourses(requesterId);
		List<CourseListResponse> courseListResponses = new ArrayList<>();

		for (Course c : courses) {
			CourseListResponse courseListResponse = CourseListResponse.builder()
					.id(c.getId())
					.name(c.getName())
					.professor(c.getProfessor())
					.build();

			courseListResponses.add(courseListResponse);
		}

		return CommonResponse.builder()
				.status(HttpStatus.OK)
				.message("수강중인 강좌 목록조회 성공")
				.data(courseListResponses)
				.build();
	}

	@GetMapping("/all")
	public ApiResponse<?> getCoursesByEnabled() {
		List<Course> courses = courseServiceForApi.getCoursesByEnabled();
		List<CourseListResponse> courseListResponses = new ArrayList<>();

		for (Course c : courses) {
			CourseListResponse courseListResponse = CourseListResponse.builder()
					.id(c.getId())
					.name(c.getName())
					.professor(c.getProfessor())
					.build();

			courseListResponses.add(courseListResponse);
		}

		return CommonResponse.builder()
				.status(HttpStatus.OK)
				.message("강좌 목록조회 성공")
				.data(courseListResponses)
				.build();
	}

	@PostMapping
	public ApiResponse<?> registerCourse(@RequestAttribute Long requesterId, @RequestParam("courseId") Long courseId) {
		Student student = courseServiceForApi.registerCourse(requesterId, courseId);

		return CommonResponse.builder()
				.status(HttpStatus.CREATED)
				.message("강좌 수강신청 성공")
				.build();
	}

}
