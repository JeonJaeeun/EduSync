package org.edusync.tutor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamScoreResponse {
    private Long id;
    private String subject;
    private Float score;
    private String grade;
    private Integer unit;
} 