package com.shouwn.oj.model.response;

import com.shouwn.oj.model.enums.ProblemType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseMainProblemResponse {

	private Long courseId;

	private Long problemId;

	private ProblemType problemType;

	private int resolvedProblemCount;

	private int unresolvedProblemCount;

	private int totalProblemCount;
}
