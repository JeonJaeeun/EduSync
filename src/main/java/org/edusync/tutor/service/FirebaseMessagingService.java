package org.edusync.tutor.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.firebase.messaging.*;
import org.edusync.tutor.entity.FcmToken;
import org.edusync.tutor.repository.FcmTokenRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FirebaseMessagingService {

    private final FcmTokenRepository fcmTokenRepository;

    public FirebaseMessagingService(FcmTokenRepository fcmTokenRepository) {
        this.fcmTokenRepository = fcmTokenRepository;
    }

    /**
     * FCM 토큰 저장 또는 업데이트
     */
    @Transactional
    public void saveOrUpdateToken(String userId, String token) {
        List<FcmToken> tokens = fcmTokenRepository.findByUserId(userId);

        if (tokens.isEmpty()) {
            fcmTokenRepository.save(new FcmToken(userId, token));
        } else {
            for (FcmToken fcmToken : tokens) {
                if (!fcmToken.getToken().equals(token)) { // 변경될 때만 저장
                    fcmToken.setToken(token);
                    fcmTokenRepository.save(fcmToken);
                }
            }
        }
    }

    /**
     * 단일 사용자에게 알림 전송
     */
    public String sendNotification(String token, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("FCM Message Sent: " + response);
            return response;
        } catch (FirebaseMessagingException e) {
            System.err.println("FCM Error: " + e.getMessagingErrorCode());
            return e.getMessagingErrorCode().toString();
        }
    }


    /**
     * 특정 사용자(userId)에게 푸시 알림 전송
     */
    @Transactional
    public void sendNotificationToUser(String userId, String title, String body) {
        List<FcmToken> tokens = fcmTokenRepository.findByUserId(userId);
        tokens.forEach(token -> sendNotification(token.getToken(), title, body));
    }

    /**
     * 여러 사용자에게 한 번에 푸시 알림 전송
     */
    @Transactional
    public void sendNotificationToMultipleUsers(List<String> userIds, String title, String body) {
        List<FcmToken> tokens = fcmTokenRepository.findByUserIdIn(userIds);
        List<Message> messages = tokens.stream()
                .map(token -> Message.builder()
                        .setToken(token.getToken())
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .build())
                .collect(Collectors.toList());

        if (!messages.isEmpty()) {
            try {
                BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
                // 실패한 토큰 제거
                for (int i = 0; i < response.getResponses().size(); i++) {
                    if (!response.getResponses().get(i).isSuccessful()) {
                        fcmTokenRepository.deleteByToken(tokens.get(i).getToken());
                    }
                }
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException("FCM 메시지 전송 실패", e);
            }
        }
    }
}