package org.edusync.tutor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.edusync.tutor.config.TestConfig;
import org.edusync.tutor.config.TestContainerConfig;
import org.edusync.tutor.dto.ExamRequest;
import org.edusync.tutor.dto.ExamScoreRequest;
import org.edusync.tutor.entity.Exam;
import org.edusync.tutor.entity.Lesson;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.entity.UserType;
import org.edusync.tutor.repository.ExamRepository;
import org.edusync.tutor.repository.LessonRepository;
import org.edusync.tutor.repository.UserRepository;
import org.edusync.tutor.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
    classes = {TestContainerConfig.class, TestConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ExamScoreControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ExamScoreControllerTest.class);
 
    static {
        System.setProperty("spring.profiles.active", "test");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private User teacher;
    private User student;
    private Lesson lesson;
    private Exam exam;
    private String teacherToken;
    private String studentToken;
    private ExamScoreRequest scoreRequest;

    @BeforeEach
    void setUp() {
        logger.info("=== Test Setup Starting ===");
        try {
            mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
            logger.info("MockMvc setup completed");

            objectMapper.registerModule(new JavaTimeModule());
            logger.info("ObjectMapper setup completed");

            // 선생님 계정 생성
            teacher = userRepository.save(User.builder()
                .email("teacher@test.com")
                .password(passwordEncoder.encode("password"))
                .nickname("Teacher")
                .userType(UserType.TEACHER)
                .build());
            logger.info("Teacher created with ID: {}", teacher.getId());

            // 학생 계정 생성
            student = userRepository.save(User.builder()
                .email("student@test.com")
                .password(passwordEncoder.encode("password"))
                .nickname("Student")
                .userType(UserType.STUDENT)
                .build());
            logger.info("Student created with ID: {}", student.getId());

            // 수업 생성
            lesson = lessonRepository.save(Lesson.builder()
                .teacher(teacher)
                .student(student)
                .subject("수학")
                .lessonDay("MON")
                .lessonStartTime(LocalTime.of(14, 0))
                .lessonEndTime(LocalTime.of(16, 0))
                .tuition(300000)
                .tuitionCycle("monthly")
                .build());
            logger.info("Lesson created with ID: {}", lesson.getId());

            // 시험 생성
            exam = examRepository.save(Exam.builder()
                .lesson(lesson)
                .createdBy(teacher)
                .examType("내신")
                .examName("1학기 중간고사")
                .examDate(LocalDate.now())
                .build());
            logger.info("Exam created with ID: {}", exam.getId());

            // 토큰 생성
            teacherToken = "Bearer " + jwtTokenProvider.createToken(
                teacher.getEmail(),
                Collections.singletonList("ROLE_" + teacher.getUserType().name())
            );
            logger.info("Teacher token created: {}", teacherToken);

            studentToken = "Bearer " + jwtTokenProvider.createToken(
                student.getEmail(),
                Collections.singletonList("ROLE_" + student.getUserType().name())
            );
            logger.info("Student token created: {}", studentToken);

            // 성적 요청 객체 생성
            scoreRequest = new ExamScoreRequest();
            scoreRequest.setSubject("수학");
            scoreRequest.setScore(95);
            scoreRequest.setGrade("A");
            scoreRequest.setUnit(3);
            logger.info("ExamScoreRequest created");

        } catch (Exception e) {
            logger.error("Error during setup: ", e);
            throw new RuntimeException("Test setup failed", e);
        }
        logger.info("=== Test Setup Completed ===");
    }

    @Test
    void addScore_Success() throws Exception {
        logger.info("Starting addScore_Success test");
        mockMvc.perform(post("/api/exams/{examId}/scores", exam.getId())
                .header("Authorization", teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoreRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("성적이 성공적으로 등록되었습니다."))
            .andExpect(jsonPath("$.scoreId").exists());
    }

    @Test
    void addScore_AsStudent_Success() throws Exception {
        mockMvc.perform(post("/api/exams/{examId}/scores", exam.getId())
                .header("Authorization", studentToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoreRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("성적이 성공적으로 등록되었습니다."))
            .andExpect(jsonPath("$.scoreId").exists());
    }

    @Test
    void addScore_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/exams/{examId}/scores", exam.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoreRequest)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getScores_AsTeacher_Success() throws Exception {
        // 먼저 성적 등록
        mockMvc.perform(post("/api/exams/{examId}/scores", exam.getId())
                .header("Authorization", teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoreRequest)))
            .andExpect(status().isCreated());

        // 성적 조회
        mockMvc.perform(get("/api/exams/{examId}/scores", exam.getId())
                .header("Authorization", teacherToken))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getScores_AsStudent_Success() throws Exception {
        // 먼저 성적 등록
        mockMvc.perform(post("/api/exams/{examId}/scores", exam.getId())
                .header("Authorization", teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoreRequest)))
            .andExpect(status().isCreated());

        // 학생으로 성적 조회
        mockMvc.perform(get("/api/exams/{examId}/scores", exam.getId())
                .header("Authorization", studentToken))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("jwt.secret", () -> "testsecretkeytestsecretkeytestsecretkeytestsecretkey");
        registry.add("jwt.expiration", () -> "3600000");
    }
} 