package org.edusync.tutor.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.edusync.tutor.entity.VerificationToken;
import org.edusync.tutor.repository.VerificationTokenRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final VerificationTokenRepository tokenRepository;

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(text, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("your-email@example.com");
        mailSender.send(mimeMessage);
    }

    public String sendVerificationCode(String email) throws MessagingException {
        String verificationCode = generateCode();
        String subject = "Password Reset Verification Code";
        String text = "Your verification code is: " + verificationCode;

        // Save verification code with expiry time
        VerificationToken token = new VerificationToken();
        token.setEmail(email);
        token.setVerificationCode(verificationCode);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(3));
        tokenRepository.save(token);

        sendEmail(email, subject, text);
        return verificationCode;
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}