package com.shouwn.oj.model.response;

import com.shouwn.oj.model.entity.problem.Course;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseListResponse {

	private Long id;

	private String name;

	private String professorName;

	@Builder
	public CourseListResponse(Course course) {
		this.id = course.getId();
		this.name = course.getName();
		this.professorName = course.getProfessor().getName();
	}
}
