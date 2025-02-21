package org.edusync.tutor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.edusync.tutor.dto.ExamScoreRequest;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.entity.UserType;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.edusync.tutor.repository.UserRepository;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ExamScoreControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(ExamScoreControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String teacherToken;
    private String studentToken;
    private User teacher;
    private User student;
    private ExamScoreRequest scoreRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();

        // 선생님 계정 생성
        teacher = User.builder()
            .email("teacher@test.com")
            .password(passwordEncoder.encode("password"))
            .nickname("Teacher")
            .userType(UserType.TEACHER)
            .build();
        teacher = userRepository.save(teacher);
        teacherToken = "Bearer " + jwtTokenProvider.createToken(
            teacher.getEmail(),
            Collections.singletonList("ROLE_TEACHER")
        );

        // 학생 계정 생성
        student = User.builder()
            .email("student@test.com")
            .password(passwordEncoder.encode("password"))
            .nickname("Student")
            .userType(UserType.STUDENT)
            .build();
        student = userRepository.save(student);
        studentToken = "Bearer " + jwtTokenProvider.createToken(
            student.getEmail(),
            Collections.singletonList("ROLE_STUDENT")
        );

        // 성적 요청 객체 생성
        scoreRequest = new ExamScoreRequest();
        scoreRequest.setSubject("수학");
        scoreRequest.setScore(95);
        scoreRequest.setGrade("A");
        scoreRequest.setUnit(3);

        logger.info("=== Test Setup Completed ===");
    }

    @Test
    void addScore_Success() throws Exception {
        mockMvc.perform(post("/api/exams/{examId}/scores", 1L)
                .header("Authorization", teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoreRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("성적이 성공적으로 등록되었습니다."))
            .andExpect(jsonPath("$.scoreId").exists());
    }

    @Test
    void addScore_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/exams/{examId}/scores", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoreRequest)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getScores_Success() throws Exception {
        mockMvc.perform(get("/api/exams/{examId}/scores", 1L)
                .header("Authorization", teacherToken))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getScores_AsStudent_Success() throws Exception {
        mockMvc.perform(get("/api/exams/{examId}/scores", 1L)
                .header("Authorization", studentToken))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
} 