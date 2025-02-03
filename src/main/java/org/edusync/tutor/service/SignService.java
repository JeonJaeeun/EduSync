package org.edusync.tutor.service;

import org.edusync.tutor.dto.SignInResultDto;
import org.edusync.tutor.dto.SignUpResultDto;

public interface SignService {
    SignUpResultDto signUp(String email, String password, String nickname, String phoneNumber, String schoolName, String userType, String role);
    SignInResultDto signIn(String email, String password) throws RuntimeException;
}