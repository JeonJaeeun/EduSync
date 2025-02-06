package org.edusync.tutor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamScoreRequest {
    private Long examId;
    private String subject;
    private Float score;
    private String grade;
    private Integer unit;
} 