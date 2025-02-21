package org.edusync.tutor.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FcmRequest {
    private String userEmail;
    private String title;
    private String body;
    private List<String> userEmails;
}