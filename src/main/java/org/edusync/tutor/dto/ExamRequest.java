package org.edusync.tutor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ExamRequest {
    private Long lessonId;
    @NotBlank(message = "시험 유형은 필수입니다")
    private String examType;
    @NotBlank(message = "시험 이름은 필수입니다")
    private String examName;
    @NotNull(message = "시험 날짜는 필수입니다")
    private LocalDate examDate;
} 