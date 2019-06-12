package com.shouwn.oj.controller.problem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.shouwn.oj.model.entity.member.Student;
import com.shouwn.oj.model.entity.problem.Course;
import com.shouwn.oj.model.entity.problem.Problem;
import com.shouwn.oj.model.enums.ProblemType;
import com.shouwn.oj.model.response.ApiResponse;
import com.shouwn.oj.model.response.CommonResponse;
import com.shouwn.oj.model.response.CourseInfoResponse;
import com.shouwn.oj.model.response.ProblemCountResponse;
import com.shouwn.oj.model.response.course.CourseListResponse;
import com.shouwn.oj.security.CurrentUser;
import com.shouwn.oj.service.member.StudentServiceForApi;
import com.shouwn.oj.service.problem.CourseServiceForApi;
import com.shouwn.oj.service.problem.ProblemServiceForApi;
import one.util.streamex.EntryStream;

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
	public ApiResponse<?> getRegisteredCourses(@CurrentUser Long memberId) {
		List<Course> courses = courseServiceForApi.getRegisteredCourses(memberId);
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
	public ApiResponse<?> registerCourse(@CurrentUser Long memberId, @PathVariable("courseId") Long courseId) {
		Student student = courseServiceForApi.registerCourse(memberId, courseId);

		return CommonResponse.builder()
				.status(HttpStatus.CREATED)
				.message("강좌 수강신청 성공")
				.build();
	}

	@GetMapping("/courses/{courseId}")
	public ApiResponse<?> getCourseInformation(@PathVariable("courseId") Long courseId) {
		Course course = courseServiceForApi.getCourseById(courseId);

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

	@GetMapping("/courses/{courseId}/categories")
	public ApiResponse<?> getCourseMainProblems(@CurrentUser Long requesterId, @PathVariable("courseId") Long courseId) {
		Course course = courseServiceForApi.getCourseById(courseId);
		Student student = studentServiceForApi.findById(requesterId);

		Map<ProblemType, List<Problem>> problemsGroupByType = course.getProblems().stream()
				.collect(Collectors.groupingBy(Problem::getType));

		Map<ProblemType, ProblemCountResponse> problemCountResponses = EntryStream.of(problemsGroupByType)
				.mapToValue((type, problems) -> ProblemCountResponse.builder()
						.resolvedCount(problemServiceForApi.getResolvedProblemCount(student, problems))
						.totalCount(problems.size())
						.build())
				.toMap();

		return CommonResponse.builder()
				.status(HttpStatus.OK)
				.message("문제 카테고리 조회 성공")
				.data(problemCountResponses)
				.build();
	}

}
