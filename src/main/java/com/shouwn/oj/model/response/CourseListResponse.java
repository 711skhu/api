package com.shouwn.oj.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseListResponse {

	private Long id;

	private String name;

	private String professorName;

	@Builder
	public CourseListResponse(Long id, String name, String professorName) {
		this.id = id;
		this.name = name;
		this.professorName = professorName;
	}
}
