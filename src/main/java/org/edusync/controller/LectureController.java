package org.edusync.controller;

import org.edusync.dto.LectureRequest;
import org.edusync.entity.Lecture;
import org.edusync.service.LectureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {
    
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> createLecture(@RequestBody LectureRequest request) {
        Lecture savedLecture = lectureService.createLecture(request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Lecture created successfully");
        response.put("lectureId", savedLecture.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Lecture>> getAllLectures() {
        List<Lecture> lectures = lectureService.getAllLectures();
        return ResponseEntity.ok(lectures);
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<Lecture> getLecture(@PathVariable Long lectureId) {
        Lecture lecture = lectureService.getLecture(lectureId);
        return ResponseEntity.ok(lecture);
    }

    @PutMapping("/{lectureId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String, String>> updateLecture(
            @PathVariable Long lectureId,
            @RequestBody LectureRequest request) {
        lectureService.updateLecture(lectureId, request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Lecture updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{lectureId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String, String>> deleteLecture(@PathVariable Long lectureId) {
        lectureService.deleteLecture(lectureId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Lecture deleted successfully");
        return ResponseEntity.ok(response);
    }
} 