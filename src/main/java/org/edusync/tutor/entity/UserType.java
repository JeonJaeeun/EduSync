package org.edusync.tutor.entity;

public enum UserType {
    STUDENT("학생"),
    PARENT("학부모"),
    TEACHER("선생님");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
