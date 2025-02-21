package org.edusync.tutor.service;

import lombok.RequiredArgsConstructor;
import org.edusync.tutor.dto.ClassJournalRequest;
import org.edusync.tutor.dto.ClassJournalResponse;
import org.edusync.tutor.entity.ClassJournal;
import org.edusync.tutor.entity.Lesson;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.exception.ResourceNotFoundException;
import org.edusync.tutor.exception.UnauthorizedException;
import org.edusync.tutor.repository.ClassJournalRepository;
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
public class ClassJournalService {
    private final ClassJournalRepository journalRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    public ClassJournalResponse createJournal(Long lessonId, ClassJournalRequest request) {
        User currentUser = getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("수업을 찾을 수 없습니다."));

        if (!lesson.getTeacher().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("수업일지를 작성할 권한이 없습니다.");
        }

        if (journalRepository.existsByLessonIdAndLessonDate(lessonId, request.getLessonDate())) {
            throw new IllegalStateException("해당 날짜의 수업일지가 이미 존재합니다.");
        }

        ClassJournal journal = ClassJournal.builder()
            .lesson(lesson)
            .lessonDate(request.getLessonDate())
            .progress(request.getProgress())
            .journalContent(request.getJournalContent())
            .homeworkCompletion(request.getHomeworkCompletion())
            .build();

        journal = journalRepository.save(journal);
        return mapToResponse(journal);
    }

    @Transactional(readOnly = true)
    public List<ClassJournalResponse> getJournals(Long lessonId) {
        User currentUser = getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("수업을 찾을 수 없습니다."));

        validateAccess(currentUser, lesson);

        return journalRepository.findByLessonId(lessonId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClassJournalResponse getJournal(Long journalId) {
        ClassJournal journal = journalRepository.findById(journalId)
            .orElseThrow(() -> new ResourceNotFoundException("수업일지를 찾을 수 없습니다."));

        User currentUser = getCurrentUser();
        validateAccess(currentUser, journal.getLesson());

        return mapToResponse(journal);
    }

    public ClassJournalResponse updateJournal(Long journalId, ClassJournalRequest request) {
        ClassJournal journal = journalRepository.findById(journalId)
            .orElseThrow(() -> new ResourceNotFoundException("수업일지를 찾을 수 없습니다."));

        User currentUser = getCurrentUser();
        if (!journal.getLesson().getTeacher().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("수업일지를 수정할 권한이 없습니다.");
        }

        journal.setLessonDate(request.getLessonDate());
        journal.setProgress(request.getProgress());
        journal.setJournalContent(request.getJournalContent());
        journal.setHomeworkCompletion(request.getHomeworkCompletion());

        journal = journalRepository.save(journal);
        return mapToResponse(journal);
    }

    public void deleteJournal(Long journalId) {
        ClassJournal journal = journalRepository.findById(journalId)
            .orElseThrow(() -> new ResourceNotFoundException("수업일지를 찾을 수 없습니다."));

        User currentUser = getCurrentUser();
        if (!journal.getLesson().getTeacher().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("수업일지를 삭제할 권한이 없습니다.");
        }

        journalRepository.delete(journal);
    }

    private void validateAccess(User user, Lesson lesson) {
        boolean hasAccess = lesson.getTeacher().getId().equals(user.getId()) ||
                          lesson.getStudent().getId().equals(user.getId());
        if (!hasAccess) {
            throw new UnauthorizedException("수업일지에 접근할 권한이 없습니다.");
        }
    }

    private ClassJournalResponse mapToResponse(ClassJournal journal) {
        return ClassJournalResponse.builder()
            .id(journal.getId())
            .lessonId(journal.getLesson().getId())
            .lessonDate(journal.getLessonDate())
            .progress(journal.getProgress())
            .journalContent(journal.getJournalContent())
            .homeworkCompletion(journal.getHomeworkCompletion())
            .createdAt(journal.getCreatedAt())
            .updatedAt(journal.getUpdatedAt())
            .build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UnauthorizedException("인증 정보를 찾을 수 없습니다."));
    }
} 