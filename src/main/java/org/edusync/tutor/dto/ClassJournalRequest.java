package org.edusync.tutor.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ClassJournalRequest {
    private Long lessonId;
    private LocalDate lessonDate;
    private String progress;
    private String journalContent;
    private Integer homeworkCompletion;
} 