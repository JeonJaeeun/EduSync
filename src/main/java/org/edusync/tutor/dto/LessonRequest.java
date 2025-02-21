package org.edusync.tutor.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
public class LessonRequest {
    @NotNull(message = "학생 ID는 필수입니다")
    private Long studentId;

    @NotBlank(message = "과목명은 필수입니다")
    @Size(max = 100, message = "과목명은 100자를 초과할 수 없습니다")
    private String subject;

    @NotBlank(message = "수업 요일은 필수입니다")
    @Pattern(regexp = "^(MON|TUE|WED|THU|FRI|SAT|SUN)(,(MON|TUE|WED|THU|FRI|SAT|SUN))*$",
            message = "올바른 요일 형식이 아닙니다")
    private String lessonDay;

    @NotNull(message = "수업 시작 시간은 필수입니다")
    private LocalTime lessonStartTime;

    @NotNull(message = "수업 종료 시간은 필수입니다")
    private LocalTime lessonEndTime;

    @NotNull(message = "수업료는 필수입니다")
    @Min(value = 0, message = "수업료는 0 이상이어야 합니다")
    private Integer tuition;

    @NotBlank(message = "수업료 납입 주기는 필수입니다")
    private String tuitionCycle;
} 