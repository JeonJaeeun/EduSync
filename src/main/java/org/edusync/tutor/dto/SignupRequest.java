package org.edusync.tutor.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.edusync.tutor.entity.UserType;
import org.edusync.tutor.entity.SocialProvider;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class SignupRequest {
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다")
    private String password;

    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 2, max = 50, message = "이름은 2-50자 사이여야 합니다")
    private String name;

    @Size(max = 50, message = "닉네임은 50자를 초과할 수 없습니다")
    private String nickname;

    @Pattern(regexp = "^(\\d{2,3}-\\d{3,4}-\\d{4}|\\d{10,11})$", 
            message = "올바른 전화번호 형식이 아닙니다")
    private String phoneNumber;

    @Size(max = 255, message = "학교명은 255자를 초과할 수 없습니다")
    private String schoolName;

    @Min(value = 1, message = "학년은 1 이상이어야 합니다")
    @Max(value = 6, message = "학년은 6 이하여야 합니다")
    private Integer grade;

    @Size(max = 20, message = "반 이름은 20자를 초과할 수 없습니다")
    private String className;

    @NotNull(message = "사용자 유형은 필수입니다")
    private UserType userType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private SocialProvider socialProvider = SocialProvider.NONE;
    private String socialId;
    private String profileImageUrl;
} 