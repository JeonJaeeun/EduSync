package org.edusync.tutor.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponse {
    private Long id;
    private Long teacherId;
    private String teacherNickname;
    private Long studentId;
    private String studentNickname;
    private String subject;
    private String lessonDay;
    private LocalTime lessonStartTime;
    private LocalTime lessonEndTime;
    private Integer tuition;
    private String tuitionCycle;
    private Boolean isActive;
} 