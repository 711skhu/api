package com.shouwn.oj.service.problem;

import java.util.List;

import com.shouwn.oj.model.entity.member.Student;
import com.shouwn.oj.model.entity.problem.Problem;

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
		int count = 0;

		for (Problem problem : problems) {
			int score = solutionService.getStudentScoreInProblemDetails(student, problem.getProblemDetails());

			if (problem.fullScore() == score) {
				count++;
			}
		}

		return count;
	}
}
