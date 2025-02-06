package org.edusync.dto.auth;

public class LoginResponse {
    private String token;
    private String userType;

    public LoginResponse(String token, String userType) {
        this.token = token;
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public String getUserType() {
        return userType;
    }
} 