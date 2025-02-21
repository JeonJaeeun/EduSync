package org.edusync.tutor.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponse {
    private Long id;
    private Long lessonId;
    private String examType;
    private String examName;
    private LocalDate examDate;
    private List<ExamScoreResponse> scores;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 