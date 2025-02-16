package org.edusync.tutor.controller;

import lombok.RequiredArgsConstructor;
import org.edusync.tutor.dto.ClassJournalRequest;
import org.edusync.tutor.dto.ClassJournalResponse;
import org.edusync.tutor.service.ClassJournalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/classes/{lessonId}/journals")
@RequiredArgsConstructor
public class ClassJournalController {

    private final ClassJournalService journalService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String, Object>> createJournal(
            @PathVariable Long lessonId,
            @Valid @RequestBody ClassJournalRequest request) {
        ClassJournalResponse journal = journalService.createJournal(lessonId, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "수업일지가 성공적으로 작성되었습니다.");
        response.put("journalId", journal.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ClassJournalResponse>> getJournals(@PathVariable Long lessonId) {
        return ResponseEntity.ok(journalService.getJournals(lessonId));
    }

    @GetMapping("/{journalId}")
    public ResponseEntity<ClassJournalResponse> getJournal(
            @PathVariable Long lessonId,
            @PathVariable Long journalId) {
        return ResponseEntity.ok(journalService.getJournal(journalId));
    }

    @PutMapping("/{journalId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String, String>> updateJournal(
            @PathVariable Long lessonId,
            @PathVariable Long journalId,
            @Valid @RequestBody ClassJournalRequest request) {
        journalService.updateJournal(journalId, request);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "수업일지가 성공적으로 수정되었습니다.");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{journalId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String, String>> deleteJournal(
            @PathVariable Long lessonId,
            @PathVariable Long journalId) {
        journalService.deleteJournal(journalId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "수업일지가 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }
} 