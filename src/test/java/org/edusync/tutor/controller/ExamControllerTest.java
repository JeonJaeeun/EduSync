package org.edusync.tutor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.edusync.tutor.config.TestConfig;
import org.edusync.tutor.config.TestContainerConfig;
import org.edusync.tutor.dto.ExamRequest;
import org.edusync.tutor.entity.Lesson;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.entity.UserType;
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
public class ExamControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(ExamControllerTest.class);

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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private User teacher;
    private User student;
    private Lesson lesson;
    private String teacherToken;
    private String studentToken;
    private ExamRequest examRequest;

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

            // 시험 요청 객체 생성
            examRequest = new ExamRequest();
            examRequest.setExamType("내신");
            examRequest.setExamName("1학기 중간고사");
            examRequest.setExamDate(LocalDate.now());
            logger.info("ExamRequest created");

        } catch (Exception e) {
            logger.error("Error during setup: ", e);
            throw new RuntimeException("Test setup failed", e);
        }
        logger.info("=== Test Setup Completed ===");
    }

    @Test
    void createExam_Success() throws Exception {
        logger.info("Starting createExam_Success test");
        mockMvc.perform(post("/api/classes/{lessonId}/exams", lesson.getId())
                .header("Authorization", teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("시험이 성공적으로 등록되었습니다."))
            .andExpect(jsonPath("$.examId").exists());
    }

    @Test
    void createExam_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/classes/{lessonId}/exams", lesson.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examRequest)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getExams_Success() throws Exception {
        mockMvc.perform(get("/api/classes/{lessonId}/exams", lesson.getId())
                .header("Authorization", teacherToken))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getExam_Success() throws Exception {
        // 먼저 시험을 생성
        String response = mockMvc.perform(post("/api/classes/{lessonId}/exams", lesson.getId())
                .header("Authorization", teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long examId = objectMapper.readTree(response).get("examId").asLong();

        // 생성된 시험 조회
        mockMvc.perform(get("/api/classes/{lessonId}/exams/{examId}", lesson.getId(), examId)
                .header("Authorization", teacherToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(examId));
    }

    @Test
    void getExam_AsStudent_Success() throws Exception {
        // 시험 생성
        String response = mockMvc.perform(post("/api/classes/{lessonId}/exams", lesson.getId())
                .header("Authorization", teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long examId = objectMapper.readTree(response).get("examId").asLong();

        // 학생으로 시험 조회
        mockMvc.perform(get("/api/classes/{lessonId}/exams/{examId}", lesson.getId(), examId)
                .header("Authorization", studentToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(examId));
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("jwt.secret", () -> "testsecretkeytestsecretkeytestsecretkeytestsecretkey");
        registry.add("jwt.expiration", () -> "3600000");
    }
} 