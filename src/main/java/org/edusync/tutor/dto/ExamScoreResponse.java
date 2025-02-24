package org.edusync.tutor.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamScoreResponse {
    private Long id;
    private Long examId;
    private Long studentId;
    private String subject;
    private Integer score;
    private String grade;
    private Integer unit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 