package org.edusync.tutor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    @NotBlank(message = "Email is required")
    private String email;
}