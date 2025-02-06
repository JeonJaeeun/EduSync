package org.edusync.tutor.service;

import org.edusync.tutor.dto.SignInResultDto;
import org.edusync.tutor.dto.SignUpResultDto;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.entity.UserType;
import org.edusync.tutor.exception.ResourceNotFoundException;
import org.edusync.tutor.repository.UserRepository;
import org.edusync.tutor.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignServiceImpl signService;

    private User testUser;
    private String testEmail;
    private String testPassword;
    private String testNickname;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        testPassword = "password123!";
        testNickname = "테스트유저";

        testUser = User.builder()
                .id(1L)
                .email(testEmail)
                .password("encodedPassword")
                .nickname(testNickname)
                .phoneNumber("01012345678")
                .schoolName("테스트학교")
                .userType(UserType.STUDENT)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }

    @Test
    void signUp_Success() {
        // given
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        SignUpResultDto result = signService.signUp(
                testEmail,
                testPassword,
                testNickname,
                "01012345678",
                "테스트학교",
                "STUDENT",
                "user"
        );

        // then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getCode()).isZero();
    }

    @Test
    void signUp_DuplicateEmail() {
        // given
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));

        // when
        SignUpResultDto result = signService.signUp(
                testEmail,
                testPassword,
                testNickname,
                "01012345678",
                "테스트학교",
                "STUDENT",
                "user"
        );

        // then
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMsg()).isEqualTo("이미 존재하는 이메일입니다.");
    }

    @Test
    void signIn_Success() {
        // given
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(testPassword, testUser.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createToken(anyString(), any())).thenReturn("test.jwt.token");

        // when
        SignInResultDto result = signService.signIn(testEmail, testPassword);

        // then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getToken()).isNotNull();
    }

    @Test
    void signIn_UserNotFound() {
        // given
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> 
            signService.signIn(testEmail, testPassword)
        );
    }

    @Test
    void signIn_WrongPassword() {
        // given
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(testPassword, testUser.getPassword())).thenReturn(false);

        // when & then
        assertThrows(RuntimeException.class, () -> 
            signService.signIn(testEmail, testPassword)
        );
    }

    @Test
    void signUp_AdminRole() {
        // given
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        SignUpResultDto result = signService.signUp(
                testEmail,
                testPassword,
                testNickname,
                "01012345678",
                "테스트학교",
                "STUDENT",
                "admin"
        );

        // then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getCode()).isZero();
    }
} 