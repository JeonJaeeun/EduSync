package org.edusync.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class DatabaseConfig {
    @Bean
    public CommandLineRunner checkDatabaseConnection(DataSource dataSource) {
        return args -> {
            try (Connection conn = dataSource.getConnection()) {
                System.out.println("Database connected successfully!");
                System.out.println("URL: " + conn.getMetaData().getURL());
                System.out.println("Username: " + conn.getMetaData().getUserName());
            }
        };
    }
} 