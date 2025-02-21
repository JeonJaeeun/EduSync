package org.edusync.tutor.service;

import org.edusync.tutor.entity.User;
import org.edusync.tutor.entity.VerificationToken;
import org.edusync.tutor.exception.ResourceNotFoundException;
import org.edusync.tutor.repository.UserRepository;
import org.edusync.tutor.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PasswordResetService {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public boolean verifyCodeAndResetPassword(String email, String verificationCode, String newPassword) {
        VerificationToken token = tokenRepository.findByEmailAndVerificationCode(email, verificationCode);
        if (token != null && token.getExpiryTime().isAfter(LocalDateTime.now())) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Email not found in the system."));
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            tokenRepository.deleteByEmail(email);
            return true;
        }
        return false;
    }
}