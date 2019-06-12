package com.shouwn.oj.controller.member;

import com.shouwn.oj.model.entity.member.Student;
import com.shouwn.oj.model.response.ApiResponse;
import com.shouwn.oj.model.response.CommonResponse;
import com.shouwn.oj.model.response.student.StudentInformation;
import com.shouwn.oj.security.CurrentUser;
import com.shouwn.oj.service.member.StudentServiceForApi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("students")
public class StudentController {

	private final StudentServiceForApi studentServiceForApi;

	public StudentController(StudentServiceForApi studentServiceForApi) {
		this.studentServiceForApi = studentServiceForApi;
	}

	@GetMapping("own")
	@PreAuthorize("isAuthenticated()")
	public ApiResponse<?> getSelfInformation(@CurrentUser Long requestId) {
		Student student = studentServiceForApi.findById(requestId);

		StudentInformation studentInformation = StudentInformation.builder()
				.name(student.getName())
				.username(student.getUsername())
				.email(student.getEmail())
				.build();

		return CommonResponse.builder()
				.status(HttpStatus.OK)
				.message("개인정보 조회 성공")
				.data(studentInformation)
				.build();
	}
}
