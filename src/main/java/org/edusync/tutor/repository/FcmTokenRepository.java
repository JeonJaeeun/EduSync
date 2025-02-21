package org.edusync.tutor.repository;

import org.edusync.tutor.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    List<FcmToken> findByUserId(String userId);
    List<FcmToken> findByUserIdIn(List<String> userIds);
    void deleteByToken(String token);
}
