package org.edusync.tutor.service;

import lombok.RequiredArgsConstructor;
import org.edusync.tutor.dto.ExamRequest;
import org.edusync.tutor.dto.ExamResponse;
import org.edusync.tutor.entity.Exam;
import org.edusync.tutor.entity.Lesson;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.exception.ResourceNotFoundException;
import org.edusync.tutor.repository.ExamRepository;
import org.edusync.tutor.repository.LessonRepository;
import org.edusync.tutor.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    public ExamResponse createExam(Long lessonId, ExamRequest request) {
        User currentUser = getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("수업을 찾을 수 없습니다."));

        Exam exam = Exam.builder()
            .lesson(lesson)
            .createdBy(currentUser)
            .examType(request.getExamType())
            .examName(request.getExamName())
            .examDate(request.getExamDate())
            .build();

        exam = examRepository.save(exam);
        return mapToResponse(exam);
    }

    @Transactional(readOnly = true)
    public List<ExamResponse> getExams(Long lessonId) {
        return examRepository.findByLessonId(lessonId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExamResponse getExam(Long examId) {
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new ResourceNotFoundException("시험을 찾을 수 없습니다."));
        return mapToResponse(exam);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private ExamResponse mapToResponse(Exam exam) {
        return ExamResponse.builder()
            .id(exam.getId())
            .lessonId(exam.getLesson().getId())
            .examType(exam.getExamType())
            .examName(exam.getExamName())
            .examDate(exam.getExamDate())
            .createdAt(exam.getCreatedAt())
            .updatedAt(exam.getUpdatedAt())
            .build();
    }
} 