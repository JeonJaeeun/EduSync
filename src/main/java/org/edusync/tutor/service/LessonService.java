package org.edusync.tutor.service;

import lombok.RequiredArgsConstructor;
import org.edusync.tutor.dto.LessonRequest;
import org.edusync.tutor.dto.LessonResponse;
import org.edusync.tutor.entity.Lesson;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.entity.UserType;
import org.edusync.tutor.exception.ResourceNotFoundException;
import org.edusync.tutor.exception.UnauthorizedException;
import org.edusync.tutor.mapper.LessonMapper;
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
public class LessonService {
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final LessonMapper lessonMapper;

    public LessonResponse createLesson(LessonRequest request) {
        User teacher = getCurrentUser();
        if (teacher.getUserType() != UserType.TEACHER) {
            throw new UnauthorizedException("선생님만 수업을 생성할 수 있습니다.");
        }

        User student = userRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("학생을 찾을 수 없습니다."));

        // 학생 유형 검증
        if (student.getUserType() != UserType.STUDENT) {
            throw new UnauthorizedException("학생으로 등록된 사용자만 수업에 참여할 수 있습니다.");
        }

        Lesson lesson = lessonMapper.toEntity(request, teacher, student);
        lesson = lessonRepository.save(lesson);

        return lessonMapper.toResponse(lesson);
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> getLessons() {
        User user = getCurrentUser();
        List<Lesson> lessons;

        switch (user.getUserType()) {
            case TEACHER:
                lessons = lessonRepository.findByTeacherId(user.getId());
                break;
            case STUDENT:
                lessons = lessonRepository.findByStudentId(user.getId());
                break;
            case PARENT:
                // TODO: 학부모-학생 연결 관계 구현 후 수정
                // 현재는 임시로 빈 리스트 반환
                lessons = lessonRepository.findByStudentId(user.getId());
                break;
            default:
                throw new UnauthorizedException("수업 조회 권한이 없습니다.");
        }

        return lessons.stream()
            .map(lessonMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LessonResponse getLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("수업을 찾을 수 없습니다."));
        
        User user = getCurrentUser();
        validateLessonAccess(lesson, user);

        return lessonMapper.toResponse(lesson);
    }

    public LessonResponse updateLesson(Long id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("수업을 찾을 수 없습니다."));

        User teacher = getCurrentUser();
        if (!lesson.getTeacher().getId().equals(teacher.getId())) {
            throw new UnauthorizedException("해당 수업을 수정할 권한이 없습니다.");
        }

        // 수정 가능한 필드들 업데이트
        lesson.setSubject(request.getSubject());
        lesson.setLessonDay(request.getLessonDay());
        lesson.setLessonStartTime(request.getLessonStartTime());
        lesson.setLessonEndTime(request.getLessonEndTime());
        lesson.setTuition(request.getTuition());
        lesson.setTuitionCycle(request.getTuitionCycle());

        lesson = lessonRepository.save(lesson);
        return lessonMapper.toResponse(lesson);
    }

    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("수업을 찾을 수 없습니다."));

        User teacher = getCurrentUser();
        if (!lesson.getTeacher().getId().equals(teacher.getId())) {
            throw new UnauthorizedException("해당 수업을 삭제할 권한이 없습니다.");
        }

        lessonRepository.delete(lesson);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UnauthorizedException("인증 정보를 찾을 수 없습니다."));
    }

    private void validateLessonAccess(Lesson lesson, User user) {
        boolean hasAccess = switch (user.getUserType()) {
            case TEACHER -> lesson.getTeacher().getId().equals(user.getId());
            case STUDENT -> lesson.getStudent().getId().equals(user.getId());
            case PARENT -> lesson.getStudent().getId().equals(user.getId()); // TODO: 학부모-학생 관계 확인 로직 추가
            default -> false;
        };

        if (!hasAccess) {
            throw new UnauthorizedException("해당 수업에 접근할 권한이 없습니다.");
        }
    }
} 