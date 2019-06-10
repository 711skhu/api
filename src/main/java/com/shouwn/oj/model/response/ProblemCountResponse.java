package com.shouwn.oj.model.response;

import com.shouwn.oj.model.enums.ProblemType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProblemCountResponse {

	private ProblemType problemType;

	private int totalCount;

	private int resolvedCount;

	private int unresolvedCount;

	@Builder
	public ProblemCountResponse(ProblemType problemType, int totalCount, int resolvedCount, int unresolvedCount) {
		this.problemType = problemType;
		this.totalCount = totalCount;
		this.resolvedCount = resolvedCount;
		this.unresolvedCount = unresolvedCount;
	}
}
