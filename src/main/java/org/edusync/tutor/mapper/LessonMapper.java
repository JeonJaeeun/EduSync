package org.edusync.tutor.mapper;

import org.edusync.tutor.dto.LessonRequest;
import org.edusync.tutor.dto.LessonResponse;
import org.edusync.tutor.entity.Lesson;
import org.edusync.tutor.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {
    
    public LessonResponse toResponse(Lesson lesson) {
        return LessonResponse.builder()
            .id(lesson.getId())
            .teacherId(lesson.getTeacher().getId())
            .teacherNickname(lesson.getTeacher().getNickname())
            .studentId(lesson.getStudent().getId())
            .studentNickname(lesson.getStudent().getNickname())
            .subject(lesson.getSubject())
            .lessonDay(lesson.getLessonDay())
            .lessonStartTime(lesson.getLessonStartTime())
            .lessonEndTime(lesson.getLessonEndTime())
            .tuition(lesson.getTuition())
            .tuitionCycle(lesson.getTuitionCycle())
            .isActive(lesson.getIsActive())
            .build();
    }

    public Lesson toEntity(LessonRequest request, User teacher, User student) {
        return Lesson.builder()
            .teacher(teacher)
            .student(student)
            .subject(request.getSubject())
            .lessonDay(request.getLessonDay())
            .lessonStartTime(request.getLessonStartTime())
            .lessonEndTime(request.getLessonEndTime())
            .tuition(request.getTuition())
            .tuitionCycle(request.getTuitionCycle())
            .isActive(true)
            .build();
    }
} 