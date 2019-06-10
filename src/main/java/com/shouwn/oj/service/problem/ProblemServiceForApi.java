package com.shouwn.oj.service.problem;

import java.util.List;

import com.shouwn.oj.model.entity.member.Student;
import com.shouwn.oj.model.entity.problem.Problem;
import com.shouwn.oj.model.entity.problem.ProblemDetail;
import com.shouwn.oj.model.entity.problem.Solution;
import com.shouwn.oj.model.entity.problem.TestCase;

import org.springframework.stereotype.Service;

@Service
public class ProblemServiceForApi {

	private ProblemService problemService;

	private SolutionService solutionService;

	public ProblemServiceForApi(ProblemService problemService, SolutionService solutionService) {
		this.problemService = problemService;
		this.solutionService = solutionService;
	}

	public int getResolvedProblemCount(Student student, List<Problem> problems) {
		int resolvedCount = 0;

		if (problems.size() == 0) {
			return resolvedCount;
		}

		for (Problem problem : problems) {
			List<ProblemDetail> problemDetails = problem.getProblemDetails();

			if (problemDetails.isEmpty()) {
				continue;
			}

			if (problemDetails.size() == resolvedProblemDetailCount(problemDetails, student)) {
				++resolvedCount;
			}
		}

		return resolvedCount;
	}

	public int resolvedProblemDetailCount(List<ProblemDetail> problemDetails, Student student) {
		int resolvedCount = 0;

		for (ProblemDetail problemDetail : problemDetails) {
			List<TestCase> testCases = problemDetail.getTestCases();
			List<Solution> solutions = solutionService.findSolutionsByProblemDetailAndMember(problemDetail, student);

			if (solutions.isEmpty()) {
				continue;
			}

			if (testCases.size() == solutions.get(0).getScore()) {
				++resolvedCount;
			}
		}

		return resolvedCount;
	}
}
