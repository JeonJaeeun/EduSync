package org.edusync.tutor.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResponse {
    SUCCESS(0, "성공했습니다."),
    FAIL(-1, "실패했습니다.");

    private final int code;
    private final String msg;
}