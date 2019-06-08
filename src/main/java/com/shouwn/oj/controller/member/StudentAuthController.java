package com.shouwn.oj.controller.member;

import com.shouwn.oj.exception.InvalidParameterException;
import com.shouwn.oj.model.entity.member.Student;
import com.shouwn.oj.model.request.auth.ReissueTokenRequest;
import com.shouwn.oj.model.request.member.MemberLoginRequest;
import com.shouwn.oj.model.request.student.StudentSignUpRequest;
import com.shouwn.oj.model.response.ApiDataBuilder;
import com.shouwn.oj.model.response.ApiResponse;
import com.shouwn.oj.model.response.CommonResponse;
import com.shouwn.oj.security.JwtContext;
import com.shouwn.oj.security.JwtProvider;
import com.shouwn.oj.service.member.StudentAuthServiceForApi;
import com.shouwn.oj.service.member.StudentServiceForApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/auth")
public class StudentAuthController {

	private final JwtProvider jwtProvider;

	private final StudentServiceForApi studentService;

	private final StudentAuthServiceForApi studentAuthService;

	public StudentAuthController(JwtProvider jwtProvider,
								 StudentServiceForApi studentService,
								 StudentAuthServiceForApi studentAuthService) {
		this.jwtProvider = jwtProvider;
		this.studentService = studentService;
		this.studentAuthService = studentAuthService;
	}

	@PostMapping("token")
	public ApiResponse<?> reissueToken(@RequestBody ReissueTokenRequest reissueTokenRequest) {
		JwtContext tokenContext = jwtProvider.getContextFromJwt(reissueTokenRequest.getRefreshToken());

		if (!tokenContext.isRefreshToken()) {
			throw new InvalidParameterException("리프레시 토큰이 아닙니다.");
		}

		String token = jwtProvider.generateJwt(tokenContext);

		return CommonResponse.builder()
				.status(HttpStatus.CREATED)
				.message("토큰 재발급 완료")
				.data(new ApiDataBuilder().addData("token", token).packaging())
				.build();
	}

	@PostMapping("signUp")
	public ApiResponse<?> signUp(@RequestBody StudentSignUpRequest signUpRequest) {
		studentAuthService.checkPasswordStrength(signUpRequest.getPassword());

		studentService.makeStudent(
				signUpRequest.getName(),
				signUpRequest.getUsername(),
				studentAuthService.passwordEncode(signUpRequest.getPassword()),
				signUpRequest.getEmail()
		);

		return CommonResponse.builder()
				.status(HttpStatus.CREATED)
				.message("학생 생성 성공")
				.build();
	}

	@PostMapping("login")
	public ApiResponse<?> login(@RequestBody MemberLoginRequest loginRequest) {
		Student student = studentAuthService.login(loginRequest.getUsername(), loginRequest.getPassword());

		String token = jwtProvider.generateJwt(student.getId(), student.getRole());
		String refreshToken = jwtProvider.generateRefreshJwt(student.getId(), student.getRole());

		return CommonResponse.builder()
				.status(HttpStatus.CREATED)
				.message("로그인 성공")
				.data(new ApiDataBuilder()
						.addData("token", token)
						.addData("refreshToken", refreshToken)
						.packaging())
				.build();
	}
}
