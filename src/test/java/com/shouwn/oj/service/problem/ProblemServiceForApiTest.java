package com.shouwn.oj.service.problem;

import java.util.Arrays;
import java.util.Optional;

import com.shouwn.oj.model.entity.problem.*;
import com.shouwn.oj.model.enums.ProblemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProblemServiceForApiTest {

	@Mock
	private ProblemService problemService;

	@Mock
	private SolutionService solutionService;

	private ProblemServiceForApi problemServiceForApi;

	private Course course;

	private Problem problem;

	private ProblemDetail problemDetail;

	private TestCase testCase;

	private Solution solution;

	@BeforeEach
	void init() {
		this.problemServiceForApi = new ProblemServiceForApi(this.problemService, this.solutionService);

		this.course = Course.builder()
				.name("test")
				.description("test")
				.enabled(true)
				.professor(null)
				.build();

		this.problem = Problem.builder()
				.title("test")
				.type(ProblemType.PRACTICE)
				.course(this.course)
				.build();

		this.problemDetail = ProblemDetail.builder()
				.title("test")
				.content("test")
				.sequence(1)
				.problem(this.problem)
				.build();

		this.problem.getProblemDetails().add(this.problemDetail);

		this.testCase = TestCase.builder()
				.params("1")
				.result("1")
				.problemDetail(this.problemDetail)
				.build();

		this.problemDetail.getTestCases().add(this.testCase);

		this.solution = Solution.builder()
				.content("test")
				.score(1)
				.member(null)
				.problemDetail(this.problemDetail)
				.build();

		this.problemDetail.getSolutions().add(this.solution);
	}

	@Test
	public void countResolvedProblemSuccess() {
		when(problemService.findById(any())).thenReturn(Optional.of(this.problem));
		when(solutionService.findSolutionsByProblemDetailOrderByIdDesc(any())).thenReturn(Arrays.asList(this.solution));

		int resolvedCount = this.problemServiceForApi.countResolvedProblem(this.problem.getId());

		assertEquals(1, resolvedCount);

		verify(problemService).findById(this.problem.getId());
		verify(solutionService).findSolutionsByProblemDetailOrderByIdDesc(this.problem.getProblemDetails().get(0));
	}
}