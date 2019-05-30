package com.shouwn.oj.model.response;

import com.shouwn.oj.model.entity.member.Admin;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseListResponse {

	private Long id;

	private String name;

	private Admin professor;

	@Builder
	public CourseListResponse(Long id, String name, Admin professor) {
		this.id = id;
		this.name = name;
		this.professor = professor;
	}
}
