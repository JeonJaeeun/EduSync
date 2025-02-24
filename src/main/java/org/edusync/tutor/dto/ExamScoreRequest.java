package org.edusync.tutor.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExamScoreRequest {
    @NotBlank(message = "과목은 필수입니다")
    private String subject;

    @NotNull(message = "점수는 필수입니다")
    @Min(value = 0, message = "점수는 0점 이상이어야 합니다")
    @Max(value = 100, message = "점수는 100점 이하여야 합니다")
    private Integer score;

    private String grade;

    private Integer unit;
} 