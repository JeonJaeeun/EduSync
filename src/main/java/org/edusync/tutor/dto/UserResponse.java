package org.edusync.tutor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.edusync.tutor.entity.SocialProvider;
import org.edusync.tutor.entity.User;
import org.edusync.tutor.entity.UserType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String schoolName;
    private Integer grade;
    private String className;
    private UserType userType;
    private SocialProvider socialProvider;
    private LocalDate birthDate;
    private String profileImageUrl;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private List<String> roles;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .schoolName(user.getSchoolName())
                .grade(user.getGrade())
                .className(user.getClassName())
                .userType(user.getUserType())
                .socialProvider(user.getSocialProvider())
                .birthDate(user.getBirthDate() != null ? 
                    user.getBirthDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate() : null)
                .profileImageUrl(user.getProfileImageUrl())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles())
                .build();
    }
} 