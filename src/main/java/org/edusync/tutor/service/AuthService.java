// AuthService.java
package org.edusync.tutor.service;

import jakarta.mail.MessagingException;
import org.edusync.tutor.dto.PasswordResetRequest;
import org.edusync.tutor.dto.PasswordResetResponse;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.exception.ResourceNotFoundException;
import org.edusync.tutor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public PasswordResetResponse sendPasswordResetCode(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email not found in the system."));

        // 인증번호 생성 및 이메일 발송 로직
        String verificationCode = "123456"; // 실제로는 랜덤 생성
        String subject = "Password Reset Verification Code";
        String text = "Your verification code is: " + verificationCode;

        try {
            emailService.sendEmail(user.getEmail(), subject, text);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }

        return PasswordResetResponse.builder()
                .message("Verification code sent to email.")
                .build();
    }
}