package com.shouwn.oj.controller.problem;

import com.shouwn.oj.dto.ProblemListResDto;
import com.shouwn.oj.model.entity.problem.Problem;
import com.shouwn.oj.model.enums.ProblemType;
import com.shouwn.oj.service.problem.ProblemService;
import com.shouwn.oj.service.problem.ProblemServiceForApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProblemController {
    private ProblemService problemService;
    private ProblemServiceForApi problemServiceForApi;
    /**
     * 문제 상세 목록
     *
     * @param courseId 조회할 강좌 id
     * @throws Exception security 추후 수정
     * @see /api/{courseId}/problem
     */
    @GetMapping("/{courseId}/problem")
    public ResponseEntity<?> getProblemList(@PathVariable final Long courseId, ProblemType problemType,
                                                                 @RequestAttribute("requesterId") Long requesterId) {
        List<Problem> problemList = problemService.getProblemList(courseId,problemType,requesterId);
       // problemList = problemServiceForApi.getProblemList(problemList,requesterId);
        if(problemList==null) return new ResponseEntity<>("null", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(problemList, HttpStatus.OK);
    }
}