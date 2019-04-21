package com.shouwn.oj.dto;

import com.shouwn.oj.model.enums.ProblemType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 문제 리스트 목록
 */
@Getter
public class ProblemListResDto {

    private ProblemType type;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int score;

    @Builder
    public ProblemListResDto(ProblemType problemType, String title, LocalDateTime startDate, LocalDateTime endDate, int score){
        this.type = problemType;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.score = score;
    }

}
