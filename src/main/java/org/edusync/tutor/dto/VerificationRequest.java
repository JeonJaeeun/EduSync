package org.edusync.tutor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequest {
    private String email;
    private String verificationCode;
    private String newPassword;
}
