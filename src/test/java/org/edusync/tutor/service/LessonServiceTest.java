package org.edusync.tutor.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(LessonServiceTest.class);

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LessonMapper lessonMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LessonService lessonService;

    private User teacher;
    private User student;
    private Lesson lesson;
    private LessonRequest lessonRequest;
    private LessonResponse lessonResponse;

    @BeforeEach
    void setUp() {
        // 테스트용 데이터 설정
        teacher = User.builder()
            .id(1L)
            .email("teacher@test.com")
            .nickname("Teacher Kim")
            .userType(UserType.TEACHER)
            .build();

        student = User.builder()
            .id(2L)
            .email("student@test.com")
            .nickname("Student Lee")
            .userType(UserType.STUDENT)
            .build();

        lesson = Lesson.builder()
            .id(1L)
            .teacher(teacher)
            .student(student)
            .subject("수학")
            .lessonDay("MON")
            .lessonStartTime(LocalTime.of(14, 0))
            .lessonEndTime(LocalTime.of(16, 0))
            .tuition(300000)
            .tuitionCycle("monthly")
            .isActive(true)
            .build();

        lessonRequest = new LessonRequest();
        lessonRequest.setStudentId(2L);
        lessonRequest.setSubject("수학");
        lessonRequest.setLessonDay("MON");
        lessonRequest.setLessonStartTime(LocalTime.of(14, 0));
        lessonRequest.setLessonEndTime(LocalTime.of(16, 0));
        lessonRequest.setTuition(300000);
        lessonRequest.setTuitionCycle("monthly");

        lessonResponse = LessonResponse.builder()
            .id(1L)
            .teacherId(1L)
            .teacherNickname("Teacher Kim")
            .studentId(2L)
            .studentNickname("Student Lee")
            .subject("수학")
            .lessonDay("MON")
            .lessonStartTime(LocalTime.of(14, 0))
            .lessonEndTime(LocalTime.of(16, 0))
            .tuition(300000)
            .tuitionCycle("monthly")
            .isActive(true)
            .build();

        // SecurityContext 모의 설정
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void createLesson_WhenTeacher_ShouldCreateSuccessfully() {
        // given
        when(authentication.getName()).thenReturn("teacher@test.com");
        when(userRepository.findByEmail("teacher@test.com")).thenReturn(Optional.of(teacher));
        when(userRepository.findById(2L)).thenReturn(Optional.of(student));
        when(lessonMapper.toEntity(any(), any(), any())).thenReturn(lesson);
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);
        when(lessonMapper.toResponse(any(Lesson.class))).thenReturn(lessonResponse);

        // when
        LessonResponse response = lessonService.createLesson(lessonRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTeacherId()).isEqualTo(teacher.getId());
        assertThat(response.getStudentId()).isEqualTo(student.getId());
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void createLesson_WhenStudent_ShouldThrowUnauthorizedException() {
        // given
        logger.info("Setting up test for unauthorized lesson creation by student.");
        when(authentication.getName()).thenReturn("student@test.com");
        when(userRepository.findByEmail("student@test.com")).thenReturn(Optional.of(student));

        // when & then
        try {
            lessonService.createLesson(lessonRequest);
            logger.error("Expected UnauthorizedException was not thrown.");
        } catch (UnauthorizedException e) {
            logger.info("UnauthorizedException was thrown as expected.");
        }
    }

    @Test
    void getLessons_WhenTeacher_ShouldReturnTeacherLessons() {
        // given
        when(authentication.getName()).thenReturn("teacher@test.com");
        when(userRepository.findByEmail("teacher@test.com")).thenReturn(Optional.of(teacher));
        when(lessonRepository.findByTeacherId(teacher.getId())).thenReturn(Arrays.asList(lesson));
        when(lessonMapper.toResponse(lesson)).thenReturn(lessonResponse);

        // when
        List<LessonResponse> responses = lessonService.getLessons();

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getTeacherId()).isEqualTo(teacher.getId());
        verify(lessonRepository).findByTeacherId(teacher.getId());
    }

    @Test
    void updateLesson_WhenUnauthorized_ShouldThrowUnauthorizedException() {
        // given
        User otherTeacher = User.builder()
            .id(3L)
            .email("other.teacher@test.com")
            .userType(UserType.TEACHER)
            .build();

        when(authentication.getName()).thenReturn("other.teacher@test.com");
        when(userRepository.findByEmail("other.teacher@test.com")).thenReturn(Optional.of(otherTeacher));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        // when & then
        assertThrows(UnauthorizedException.class, () -> {
            lessonService.updateLesson(1L, lessonRequest);
        });
    }

    @Test
    void getLessons_WhenParent_ShouldReturnStudentLessons() {
        // given
        User parent = User.builder()
            .id(4L)
            .email("parent@test.com")
            .userType(UserType.PARENT)
            .build();

        when(authentication.getName()).thenReturn("parent@test.com");
        when(userRepository.findByEmail("parent@test.com")).thenReturn(Optional.of(parent));
        when(lessonRepository.findByStudentId(parent.getId())).thenReturn(Arrays.asList(lesson));
        when(lessonMapper.toResponse(lesson)).thenReturn(lessonResponse);

        // when
        List<LessonResponse> responses = lessonService.getLessons();

        // then
        assertThat(responses).hasSize(1);
        verify(lessonRepository).findByStudentId(parent.getId());
    }
} 