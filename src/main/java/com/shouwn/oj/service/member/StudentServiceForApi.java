package com.shouwn.oj.service.member;

import com.shouwn.oj.exception.member.MemberException;
import com.shouwn.oj.exception.member.PasswordIncorrectException;
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
	 * @param name		  학생 이름
	 * @param username	  학생 아이디
	 * @param rawPassword 학생 패스워드 (인코딩 전)
	 * @param email		  학생 이메일
	 *
	 * @return 생성된 관리자 객체
	 *
	 * @throws MemberException UsernameExistException 이미 아이디가 존재할 때 발생하는 예외
	 * 	                       PasswordStrengthLeakException 비밀번호가 약할 때 발생하는 예외
	 * 	                       EmailExistException 이메일이 이미 존재할 때 발생하는 예외
	 */

	public Student makeStudent(String name,
							   String username,
							   String rawPassword,
							   String email) throws MemberException {

		return studentService.makeStudent(name, username, rawPassword, email);
	}

	public Student login(String username, String rawPassword) throws MemberException {
		Student student = studentService.findByUsername(username);

		if(!studentService.isCorrectPassword(student, rawPassword)) {
			throw new PasswordIncorrectException("패스워드가 맞지 않습니다.");
		}

		return student;
	}

	public Student findById(Long id) {
		return studentService.findById(id);
	}
}
