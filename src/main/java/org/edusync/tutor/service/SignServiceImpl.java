package org.edusync.tutor.service;

import org.edusync.tutor.dto.SignInResultDto;
import org.edusync.tutor.dto.SignUpResultDto;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.entity.UserType;
import org.edusync.tutor.exception.ResourceNotFoundException;
import org.edusync.tutor.repository.UserRepository;
import org.edusync.tutor.security.CommonResponse;
import org.edusync.tutor.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public SignUpResultDto signUp(String email, String password, String nickname, String phoneNumber, String schoolName, String userType, String role) {
        LOGGER.info("[getSignUpResult] 회원 가입 정보 전달");
        
        if (userRepository.findByEmail(email).isPresent()) {
            SignUpResultDto result = new SignUpResultDto();
            setFailResult(result);
            result.setMsg("이미 존재하는 이메일입니다.");
            return result;
        }

        UserType userTypeEnum = UserType.valueOf(userType.toUpperCase());
        
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .schoolName(schoolName)
                .userType(userTypeEnum)
                .roles(role.equalsIgnoreCase("admin") 
                    ? Collections.singletonList("ROLE_ADMIN")
                    : Collections.singletonList("ROLE_USER"))
                .build();

        User savedUser = userRepository.save(user);
        SignUpResultDto signUpResultDto = new SignUpResultDto();

        if (savedUser.getId() != null) {
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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

        return SignInResultDto.builder()
                .token(token)
                .userId(user.getId())
                .role(user.getUserType().name())
                .build();
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