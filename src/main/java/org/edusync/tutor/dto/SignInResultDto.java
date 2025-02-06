package org.edusync.tutor.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResultDto {
    private boolean success;
    private int code;
    private String msg;
    private String token;
}