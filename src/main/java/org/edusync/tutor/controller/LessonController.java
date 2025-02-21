package org.edusync.tutor.controller;

import lombok.RequiredArgsConstructor;
import org.edusync.tutor.dto.LessonRequest;
import org.edusync.tutor.dto.LessonResponse;
import org.edusync.tutor.service.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class LessonController {
    
    private static final Logger logger = LoggerFactory.getLogger(LessonController.class);
    private final LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> createLesson(@Valid @RequestBody LessonRequest request) {
        logger.debug("Received create lesson request: {}", request);
        try {
            LessonResponse savedLesson = lessonService.createLesson(request);
            logger.info("Successfully created lesson with ID: {}", savedLesson.getId());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lesson created successfully");
            response.put("lessonId", savedLesson.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Failed to create lesson", e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<LessonResponse>> getLessons() {
        logger.info("Fetching lessons for current user");
        try {
            List<LessonResponse> lessons = lessonService.getLessons();
            logger.info("Found {} lessons", lessons.size());
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            logger.error("Failed to fetch lessons", e);
            throw e;
        }
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> getLesson(@PathVariable Long lessonId) {
        logger.info("Fetching lesson with ID: {}", lessonId);
        LessonResponse lesson = lessonService.getLesson(lessonId);
        return ResponseEntity.ok(lesson);
    }

    @PutMapping("/{lessonId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String, String>> updateLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonRequest request) {
        logger.info("Updating lesson with ID: {}", lessonId);
        lessonService.updateLesson(lessonId, request);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Lesson updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lessonId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String, String>> deleteLesson(@PathVariable Long lessonId) {
        logger.info("Deleting lesson with ID: {}", lessonId);
        lessonService.deleteLesson(lessonId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Lesson deleted successfully");
        return ResponseEntity.ok(response);
    }
} 