package org.edusync.tutor.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ExamRequest {
    private Long lessonId;
    private String examType;
    private String examName;
    private LocalDate examDate;
} 