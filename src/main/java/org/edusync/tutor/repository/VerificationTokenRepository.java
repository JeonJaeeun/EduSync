package org.edusync.tutor.repository;

import org.edusync.tutor.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByEmailAndVerificationCode(String email, String verificationCode);
    void deleteByEmail(String email);
}