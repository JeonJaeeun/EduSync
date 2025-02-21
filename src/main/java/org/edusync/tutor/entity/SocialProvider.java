package org.edusync.tutor.entity;

public enum SocialProvider {
    NONE("일반"),
    NAVER("네이버"),
    GOOGLE("구글"),
    KAKAO("카카오");

    private final String description;

    SocialProvider(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 