package com.shouwn.oj.service.member;

import java.util.Optional;

import com.shouwn.oj.exception.AuthenticationFailedException;
import com.shouwn.oj.exception.IllegalStateException;
import com.shouwn.oj.exception.NotFoundException;
import com.shouwn.oj.model.entity.member.Student;

import org.springframework.stereotype.Service;

@Service
public class StudentAuthServiceForApi {

	private final StudentAuthService studentAuthService;

	private final StudentService studentService;

	public StudentAuthServiceForApi(StudentAuthService studentAuthService, StudentService studentService) {
		this.studentAuthService = studentAuthService;
		this.studentService = studentService;
	}

	public Student login(String username, String rawPassword) {
		Optional<Student> student = studentService.findByUsername(username);

		if (!student.isPresent()) {
			throw new NotFoundException(username + "에 해당하는 유저가 없습니다.");
		}

		if (!studentAuthService.isCorrectPassword(student.get(), rawPassword)) {
			throw new AuthenticationFailedException("비밀번호가 다릅니다.");
		}

		return student.get();
	}

	public void checkPasswordStrength(String rawPassword) {
		if (studentAuthService.isPasswordStrengthWeak(rawPassword)) {
			throw new IllegalStateException("비밀번호 강도가 약합니다.");
		}
	}

	public String passwordEncode(String rawPassword) {
		return studentAuthService.passwordEncode(rawPassword);
	}
}
