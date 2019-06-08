package com.shouwn.oj.service.problem;

import java.util.List;

import com.shouwn.oj.exception.IllegalStateException;
import com.shouwn.oj.model.entity.problem.Problem;
import com.shouwn.oj.model.entity.problem.ProblemDetail;
import com.shouwn.oj.model.entity.problem.Solution;

import org.springframework.stereotype.Service;

@Service
public class ProblemServiceForApi {

	private ProblemService problemService;

	private SolutionService solutionService;

	public ProblemServiceForApi(ProblemService problemService, SolutionService solutionService) {
		this.problemService = problemService;
		this.solutionService = solutionService;
	}

	public int countResolvedProblem(Long problemId) {
		Problem problem = problemService.findById(problemId).orElseThrow(() -> new IllegalStateException("존재하지 않는 문제입니다."));
		List<ProblemDetail> problemDetails = problem.getProblemDetails();

		int count = 0;

		for (ProblemDetail problemDetail : problemDetails) {
			int testCaseSize = problemDetail.getTestCases().size();
			List<Solution> solutions = solutionService.findSolutionsByProblemDetailOrderByIdDesc(problemDetail);

			if (solutions.size() == 0) {
				continue;
			}

			int solutionScore = solutions.get(0).getScore();

			if (testCaseSize == solutionScore) {
				++count;
			}
		}

		return count;
	}

	public int CountTotalProblem(Long problemId) {
		Problem problem = problemService.findById(problemId).orElseThrow(() -> new IllegalStateException("존재하지 않는 문제입니다."));
		return problem.getProblemDetails().size();
	}
}
