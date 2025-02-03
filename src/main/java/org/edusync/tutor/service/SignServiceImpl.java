package org.edusync.tutor.service;

import org.edusync.tutor.dto.SignInResultDto;
import org.edusync.tutor.dto.SignUpResultDto;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.entity.UserType;
import org.edusync.tutor.repository.UserRepository;
import org.edusync.tutor.security.CommonResponse;
import org.edusync.tutor.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SignServiceImpl implements SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SignServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SignUpResultDto signUp(String email, String password, String nickname, String phoneNumber, String schoolName, String userType, String role) {
        LOGGER.info("[getSignUpResult] 회원 가입 정보 전달");
        User user;
        UserType userTypeEnum = UserType.valueOf(userType.toUpperCase());
        if (role.equalsIgnoreCase("admin")) {
            user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .nickname(nickname)
                    .phoneNumber(phoneNumber)
                    .schoolName(schoolName)
                    .userType(userTypeEnum)
                    .roles(Collections.singletonList(password))
                    .build();
        } else {
            user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .nickname(nickname)
                    .phoneNumber(phoneNumber)
                    .schoolName(schoolName)
                    .userType(userTypeEnum)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }

        User savedUser = userRepository.save(user);
        SignUpResultDto signUpResultDto = new SignUpResultDto();

        LOGGER.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과 주입");
        if (!savedUser.getEmail().isEmpty()) {
            LOGGER.info("[getSignUpResult] 정상 처리 완료");
            setSuccessResult(signUpResultDto);
        } else {
            LOGGER.info("[getSignUpResult] 실패 처리 완료");
            setFailResult(signUpResultDto);
        }
        return signUpResultDto;
    }

    @Override
    public SignInResultDto signIn(String email, String password) throws RuntimeException {
        LOGGER.info("[getSignInResult] Requesting user information with email");
        User user = userRepository.getByEmail(email);
        LOGGER.info("[getSignInResult] Email : {}", email);

        LOGGER.info("[getSignInResult] Performing password comparison");
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException();
        }

        LOGGER.info("[getSignInResult] Password match");

        LOGGER.info("[getSignInResult] Creating SignInResultDto object");
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.createToken(user.getUsername(),user.getRoles()))
                .build();

        LOGGER.info("[getSignInResult] Injecting values into SignInResultDto object");
        setSuccessResult(signInResultDto);

        return signInResultDto;
    }

    private void setSuccessResult(SignUpResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(SignUpResultDto result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }
}