package org.edusync.tutor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.edusync.tutor.config.TestConfig;
import org.edusync.tutor.dto.LessonRequest;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.entity.UserType;
import org.edusync.tutor.repository.UserRepository;
import org.edusync.tutor.security.JwtTokenProvider;
import org.edusync.tutor.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.Import;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")  // application-test.yml 사용
@Import(TestConfig.class)  // TestConfig 명시적 import
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Spring이 자동으로 설정한 MockMvc 사용

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    private User teacher;
    private User student;
    private String teacherToken;
    private String studentToken;
    private LessonRequest lessonRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        // ObjectMapper 설정
        objectMapper.registerModule(new JavaTimeModule());

        // 테스트용 사용자 생성
        teacher = userRepository.save(User.builder()
            .email("teacher@test.com")
            .password("password")
            .nickname("Teacher")
            .userType(UserType.TEACHER)
            .build());
        
        student = userRepository.save(User.builder()
            .email("student@test.com")
            .password("password")
            .nickname("Student")
            .userType(UserType.STUDENT)
            .build());

        // 실제 토큰 생성
        teacherToken = jwtTokenProvider.createToken(
            teacher.getEmail(),
            Arrays.asList("ROLE_" + teacher.getUserType().name())
        );
        
        studentToken = jwtTokenProvider.createToken(
            student.getEmail(),
            Arrays.asList("ROLE_" + student.getUserType().name())
        );

        // 테스트용 요청 객체 생성
        lessonRequest = new LessonRequest();
        lessonRequest.setStudentId(student.getId());
        lessonRequest.setSubject("수학");
        lessonRequest.setLessonDay("MON");
        lessonRequest.setLessonStartTime(LocalTime.of(14, 0));
        lessonRequest.setLessonEndTime(LocalTime.of(16, 0));
        lessonRequest.setTuition(300000);
        lessonRequest.setTuitionCycle("monthly");
    }

    private String getTestToken(String userEmail, String role) {
        return "Bearer " + jwtTokenProvider.createToken(userEmail, Collections.singletonList(role));
    }

    @Test
    void createLesson_Success() throws Exception {
        String token = getTestToken("teacher@test.com", "ROLE_TEACHER");
        
        mockMvc.perform(post("/api/classes")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lessonRequest)))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void createLesson_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/classes")
                .header("Authorization", "Bearer " + studentToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lessonRequest)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getLessons_AsTeacher() throws Exception {
        mockMvc.perform(get("/api/classes")
                .header("Authorization", "Bearer " + teacherToken))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getLesson_Success() throws Exception {
        // 먼저 수업을 생성
        String response = mockMvc.perform(post("/api/classes")
                .header("Authorization", "Bearer " + teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lessonRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        // 생성된 수업 ID 추출
        Long lessonId = objectMapper.readTree(response).get("lessonId").asLong();

        // 생성된 수업 조회
        mockMvc.perform(get("/api/classes/" + lessonId)
                .header("Authorization", "Bearer " + teacherToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(lessonId));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void updateLesson_Success() throws Exception {
        // 먼저 수업을 생성
        String response = mockMvc.perform(post("/api/classes")
                .header("Authorization", "Bearer " + teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lessonRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long lessonId = objectMapper.readTree(response).get("lessonId").asLong();

        // 수정할 내용
        lessonRequest.setSubject("영어");

        // 수업 수정
        mockMvc.perform(put("/api/classes/" + lessonId)
                .header("Authorization", "Bearer " + teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lessonRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Lesson updated successfully"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteLesson_Success() throws Exception {
        // 먼저 수업을 생성
        String response = mockMvc.perform(post("/api/classes")
                .header("Authorization", "Bearer " + teacherToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lessonRequest)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        Long lessonId = objectMapper.readTree(response).get("lessonId").asLong();

        // 수업 삭제
        mockMvc.perform(delete("/api/classes/" + lessonId)
                .header("Authorization", "Bearer " + teacherToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Lesson deleted successfully"));
    }
} 