package org.edusync.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;

// @Configuration
// @PropertySource("file:.env")
public class EnvConfig {
    
    @Value("${DB_PASSWORD}")
    private String dbPassword;
    
    // ... 다른 설정들

    // Getter 추가
    public String getDbPassword() {
        return dbPassword;
    }
} 