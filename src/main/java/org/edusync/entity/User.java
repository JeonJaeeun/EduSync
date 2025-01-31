package org.edusync.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "social_provider")
    private String socialProvider;

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSchoolName() { return schoolName; }
    public String getUserType() { return userType; }
    public String getSocialProvider() { return socialProvider; }
    public String getSocialId() { return socialId; }
    public LocalDateTime getBirthDate() { return birthDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
    public void setUserType(String userType) { this.userType = userType; }
    public void setSocialProvider(String socialProvider) { this.socialProvider = socialProvider; }
    public void setSocialId(String socialId) { this.socialId = socialId; }
    public void setBirthDate(LocalDateTime birthDate) { this.birthDate = birthDate; }
} 