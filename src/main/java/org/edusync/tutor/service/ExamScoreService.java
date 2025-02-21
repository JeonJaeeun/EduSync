package org.edusync.tutor.service;

import lombok.RequiredArgsConstructor;
import org.edusync.tutor.dto.ExamScoreRequest;
import org.edusync.tutor.dto.ExamScoreResponse;
import org.edusync.tutor.entity.Exam;
import org.edusync.tutor.entity.ExamScore;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.exception.ResourceNotFoundException;
import org.edusync.tutor.repository.ExamRepository;
import org.edusync.tutor.repository.ExamScoreRepository;
import org.edusync.tutor.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExamScoreService {
    private final ExamScoreRepository examScoreRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;

    public ExamScoreResponse addScore(Long examId, ExamScoreRequest request) {
        User currentUser = getCurrentUser();
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new ResourceNotFoundException("시험을 찾을 수 없습니다."));

        ExamScore score = ExamScore.builder()
            .exam(exam)
            .student(currentUser)
            .subject(request.getSubject())
            .score(request.getScore())
            .grade(request.getGrade())
            .unit(request.getUnit())
            .build();

        score = examScoreRepository.save(score);
        return mapToResponse(score);
    }

    @Transactional(readOnly = true)
    public List<ExamScoreResponse> getScores(Long examId) {
        return examScoreRepository.findByExamId(examId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private ExamScoreResponse mapToResponse(ExamScore score) {
        return ExamScoreResponse.builder()
            .id(score.getId())
            .examId(score.getExam().getId())
            .studentId(score.getStudent().getId())
            .subject(score.getSubject())
            .score(score.getScore())
            .grade(score.getGrade())
            .unit(score.getUnit())
            .createdAt(score.getCreatedAt())
            .updatedAt(score.getUpdatedAt())
            .build();
    }
} 