package com.shouwn.oj.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseInfoResponse {

	private Long courseId;

	private String courseName;

	private String description;

	@Builder
	public CourseInfoResponse(Long courseId, String courseName, String description) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.description = description;
	}
}
