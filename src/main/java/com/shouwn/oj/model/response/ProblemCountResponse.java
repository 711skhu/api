package com.shouwn.oj.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProblemCountResponse {

	private int totalCount;

	private int resolvedCount;

	private int unresolvedCount;

	@Builder
	public ProblemCountResponse(int totalCount, int resolvedCount) {
		this.totalCount = totalCount;
		this.resolvedCount = resolvedCount;
		this.unresolvedCount = totalCount - resolvedCount;
	}
}
