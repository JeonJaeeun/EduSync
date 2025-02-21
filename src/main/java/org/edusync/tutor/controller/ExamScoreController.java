package org.edusync.tutor.controller;

import lombok.RequiredArgsConstructor;
import org.edusync.tutor.dto.ExamScoreRequest;
import org.edusync.tutor.dto.ExamScoreResponse;
import org.edusync.tutor.service.ExamScoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/exams/{examId}/scores")
@RequiredArgsConstructor
public class ExamScoreController {

    private final ExamScoreService examScoreService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> addScore(
            @PathVariable Long examId,
            @Valid @RequestBody ExamScoreRequest request) {
        ExamScoreResponse score = examScoreService.addScore(examId, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "성적이 성공적으로 등록되었습니다.");
        response.put("scoreId", score.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExamScoreResponse>> getScores(@PathVariable Long examId) {
        return ResponseEntity.ok(examScoreService.getScores(examId));
    }
} 