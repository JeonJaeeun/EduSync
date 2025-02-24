package org.edusync.tutor.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResultDto {
    private Long userId;
    private String role;
    private String token;
}