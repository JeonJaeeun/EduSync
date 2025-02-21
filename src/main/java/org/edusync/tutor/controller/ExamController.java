package org.edusync.tutor.controller;

import lombok.RequiredArgsConstructor;
import org.edusync.tutor.dto.ExamRequest;
import org.edusync.tutor.dto.ExamResponse;
import org.edusync.tutor.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/classes/{lessonId}/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createExam(
            @PathVariable Long lessonId,
            @Valid @RequestBody ExamRequest request) {
        ExamResponse exam = examService.createExam(lessonId, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "시험이 성공적으로 등록되었습니다.");
        response.put("examId", exam.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExamResponse>> getExams(@PathVariable Long lessonId) {
        return ResponseEntity.ok(examService.getExams(lessonId));
    }

    @GetMapping("/{examId}")
    public ResponseEntity<ExamResponse> getExam(
            @PathVariable Long lessonId,
            @PathVariable Long examId) {
        return ResponseEntity.ok(examService.getExam(examId));
    }
} 