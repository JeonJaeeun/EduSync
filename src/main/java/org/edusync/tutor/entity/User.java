package org.edusync.tutor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 255)
    private String schoolName;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "class_name", length = 20)
    private String className;

    // STUDENT, PARENT, TEACHER
    @Column(name = "user_type", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserType userType;  // UserRole에서 UserType으로 변경

    @Column(name = "social_provider", length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SocialProvider socialProvider = SocialProvider.NONE;

    @Column(length = 255)
    private String socialId;

    @Temporal(TemporalType.DATE)
    private Date birthDate;  // LocalDate에서 Date로 변경

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ADMIN, USER
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private List<Lesson> teachingLessons = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private List<Lesson> learningLessons = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> authorities = new ArrayList<>(roles);
        authorities.add("ROLE_" + userType.name());
        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;  // false에서 true로 변경
    }

    public void addTeachingLesson(Lesson lesson) {
        teachingLessons.add(lesson);
        lesson.setTeacher(this);
    }

    public void addLearningLesson(Lesson lesson) {
        learningLessons.add(lesson);
        lesson.setStudent(this);
    }
} 
