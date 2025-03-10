package org.edusync.tutor.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account.path}")
    private String firebaseServiceAccountPath;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // FIREBASE_SERVICE_ACCOUNT_PATH 유무 확인
        if (firebaseServiceAccountPath == null || firebaseServiceAccountPath.isEmpty()) {
            throw new IOException("Firebase service account path not configured in application.yml");
        }
        // firebase 중복실행 방지 조건문
        if (FirebaseApp.getApps().isEmpty()) {
            FileInputStream serviceAccount =
                    new FileInputStream(firebaseServiceAccountPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }
}