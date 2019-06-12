package com.shouwn.oj.service.member;

import java.util.Optional;

import com.shouwn.oj.exception.AlreadyExistException;
import com.shouwn.oj.exception.NotFoundException;
import com.shouwn.oj.model.entity.member.Student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceForApi {

	private final StudentService studentService;

	public StudentServiceForApi(StudentService studentService) {
		this.studentService = studentService;
	}

	/**
	 * 학생을 생성하는 메소드
	 *
	 * @param name     학생 이름
	 * @param username 학생 아이디
	 * @param password 학생 패스워드 (인코딩 전)
	 * @param email    학생 이메일
	 * @return 생성된 관리자 객체
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Student makeStudent(String name,
							   String username,
							   String password,
							   String email) {
		Student student = Student.builder()
				.name(name)
				.username(username)
				.password(password)
				.email(email)
				.build();

		checkPossibleToMake(student);

		return studentService.save(student);
	}

	private void checkPossibleToMake(Student student) {
		if (studentService.isRegisteredUsername(student.getUsername())) {
			throw new AlreadyExistException(student.getUsername() + " 은 이미 등록된 아이디입니다.");
		}

		if (studentService.isRegisteredEmail(student.getEmail())) {
			throw new AlreadyExistException(student.getEmail() + " 은 이미 등록된 이메일입니다.");
		}
	}

	public Student findById(Long id) {
		Optional<Student> student = studentService.findById(id);

		if (!student.isPresent()) {
			throw new NotFoundException(id + "에 해당하는 유저가 없습니다.");
		}

		return student.get();
	}
}
