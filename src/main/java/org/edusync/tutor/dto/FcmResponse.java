package org.edusync.tutor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmResponse {
    private String message;

    public FcmResponse(String message) {
        this.message = message;
    }
}