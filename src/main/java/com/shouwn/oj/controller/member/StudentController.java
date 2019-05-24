package com.shouwn.oj.controller.member;

import com.shouwn.oj.model.entity.member.Student;
import com.shouwn.oj.model.request.StudentSignUpRequest;
import com.shouwn.oj.model.request.member.MemberLoginRequest;
import com.shouwn.oj.model.response.ApiDataBuilder;
import com.shouwn.oj.model.response.ApiResponse;
import com.shouwn.oj.model.response.CommonResponse;
import com.shouwn.oj.model.response.StudentInformation;
import com.shouwn.oj.security.JwtProvider;
import com.shouwn.oj.service.member.StudentServiceForApi;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("students")
public class StudentController {

	private final JwtProvider jwtProvider;

	private final StudentServiceForApi studentServiceForApi;

	public StudentController(JwtProvider jwtProvider, StudentServiceForApi studentServiceForApi) {
		this.jwtProvider = jwtProvider;
		this.studentServiceForApi = studentServiceForApi;
	}

	@PostMapping
	public ApiResponse<?> makeStudent(@RequestBody StudentSignUpRequest signUpRequest) {
		studentServiceForApi.makeStudent(
				signUpRequest.getName(),
				signUpRequest.getUsername(),
				signUpRequest.getPassword(),
				signUpRequest.getEmail()
		);

		return CommonResponse.builder()
				.status(HttpStatus.CREATED)
				.message("학생 생성 성공")
				.build();
	}

	@PostMapping("login")
	public ApiResponse<?> login(@RequestBody MemberLoginRequest loginRequest) {
		Student student = studentServiceForApi.login(loginRequest.getUsername(), loginRequest.getPassword());

		String jwt = jwtProvider.generateJwt(student.getId());

		return CommonResponse.builder()
				.status(HttpStatus.CREATED)
				.message("로그인 성공")
				.data(new ApiDataBuilder().addData("token", jwt).packaging())
				.build();
	}

	@GetMapping("self")
	@PreAuthorize("isAuthenticated()")
	public ApiResponse<?> getSelfInformation(@RequestAttribute Long requestId) {
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
