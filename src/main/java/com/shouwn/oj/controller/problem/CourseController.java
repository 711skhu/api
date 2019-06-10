package com.shouwn.oj.controller.problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.shouwn.oj.model.entity.member.Student;
import com.shouwn.oj.model.entity.problem.Course;
import com.shouwn.oj.model.entity.problem.Problem;
import com.shouwn.oj.model.enums.ProblemType;
import com.shouwn.oj.model.response.*;
import com.shouwn.oj.service.member.StudentServiceForApi;
import com.shouwn.oj.service.problem.CourseServiceForApi;
import com.shouwn.oj.service.problem.ProblemServiceForApi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("isAuthenticated()")
public class CourseController {

	private CourseServiceForApi courseServiceForApi;

	private StudentServiceForApi studentServiceForApi;

	private ProblemServiceForApi problemServiceForApi;

	public CourseController(CourseServiceForApi courseServiceForApi, StudentServiceForApi studentServiceForApi, ProblemServiceForApi problemServiceForApi) {
		this.courseServiceForApi = courseServiceForApi;
		this.studentServiceForApi = studentServiceForApi;
		this.problemServiceForApi = problemServiceForApi;
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
	public ApiResponse<?> getCourseInformation(@PathVariable("courseId") Long courseId) {
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

	@GetMapping("/courses/{courseId}/problems")
	public ApiResponse<?> getCourseMainProblems(@RequestAttribute Long requesterId, @PathVariable("courseId") Long courseId) {
		Map<ProblemType, List<Problem>> problemTypeListMap = courseServiceForApi.getCourseMainProblem(courseId);
		Student student = studentServiceForApi.findById(requesterId);

		List<ProblemCountResponse> problemCountResponses = new ArrayList<>();

		for (ProblemType problemType : problemTypeListMap.keySet()) {
			List<Problem> problems = problemTypeListMap.get(problemType);

			int totalCount = problems.size();
			int resolvedCount = problemServiceForApi.getResolvedProblemCount(student, problems);

			problemCountResponses.add(ProblemCountResponse.builder()
					.problemType(problemType)
					.totalCount(totalCount)
					.resolvedCount(resolvedCount)
					.unresolvedCount(totalCount - resolvedCount)
					.build()
			);
		}

		return CommonResponse.builder()
				.status(HttpStatus.OK)
				.message("문제 타입목록 조회 성공")
				.data(problemCountResponses)
				.build();
	}

}
