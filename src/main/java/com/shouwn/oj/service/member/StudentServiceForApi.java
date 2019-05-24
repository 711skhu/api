package com.shouwn.oj.service.member;

import java.util.Optional;

import com.shouwn.oj.exception.AuthenticationFailedException;
import com.shouwn.oj.exception.NotFoundException;
import com.shouwn.oj.model.entity.member.Student;

import org.springframework.stereotype.Service;

@Service
public class StudentServiceForApi {

	private final StudentService studentService;

	public StudentServiceForApi(StudentService studentService) {
		this.studentService = studentService;
	}

	/**
	 * 학생을 생성하는 메소드
	 *
	 * @param name        학생 이름
	 * @param username    학생 아이디
	 * @param rawPassword 학생 패스워드 (인코딩 전)
	 * @param email       학생 이메일
	 * @return 생성된 관리자 객체
	 */
	public Student makeStudent(String name,
							   String username,
							   String rawPassword,
							   String email) {
		return studentService.makeStudent(name, username, rawPassword, email);
	}

	public Student login(String username, String rawPassword) {
		Optional<Student> student = studentService.findByUsername(username);

		if (!student.isPresent()) {
			throw new NotFoundException(username + "에 해당하는 유저가 없습니다.");
		}

		if (!studentService.isCorrectPassword(student.get(), rawPassword)) {
			throw new AuthenticationFailedException("패스워드가 맞지 않습니다.");
		}

		return student.get();
	}

	public Student findById(Long id) {
		Optional<Student> student = studentService.findById(id);

		if (!student.isPresent()) {
			throw new NotFoundException(id + "에 해당하는 유저가 없습니다.");
		}

		return student.get();
	}
}
