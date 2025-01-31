package org.edusync.exception;

public class LectureNotFoundException extends RuntimeException {
    public LectureNotFoundException(Long id) {
        super("Could not find lecture with id: " + id);
    }
} 