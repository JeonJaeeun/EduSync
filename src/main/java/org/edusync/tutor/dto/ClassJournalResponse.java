package org.edusync.tutor.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassJournalResponse {
    private Long id;
    private Long lessonId;
    private LocalDate lessonDate;
    private String progress;
    private String journalContent;
    private Integer homeworkCompletion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 