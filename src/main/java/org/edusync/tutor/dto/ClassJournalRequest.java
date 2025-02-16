package org.edusync.tutor.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter @Setter
public class ClassJournalRequest {
    @NotNull(message = "수업 날짜는 필수입니다")
    private LocalDate lessonDate;

    @NotBlank(message = "진도는 필수입니다")
    private String progress;

    @NotBlank(message = "수업 내용은 필수입니다")
    private String journalContent;

    @Min(value = 0, message = "숙제 완성도는 0 이상이어야 합니다")
    @Max(value = 100, message = "숙제 완성도는 100 이하여야 합니다")
    private Integer homeworkCompletion;
} 