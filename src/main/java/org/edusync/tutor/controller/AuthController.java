package org.edusync.tutor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.edusync.tutor.dto.*;
import org.edusync.tutor.service.AuthService;
import org.edusync.tutor.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final SignService signService;

    @Autowired
    public AuthController(SignService signService) {
        this.signService = signService;
    }

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details.")
    public SignUpResultDto signUp(
            @Parameter(description = "Email", required = true) @RequestParam String email,
            @Parameter(description = "Password", required = true) @RequestParam String password,
            @Parameter(description = "Nickname", required = true) @RequestParam String nickname,
            @Parameter(description = "Phone Number", required = true) @RequestParam String phoneNumber,
            @Parameter(description = "School Name", required = true) @RequestParam String schoolName,
            @Parameter(description = "User Type", required = true) @RequestParam String userType,
            @Parameter(description = "Role", required = true) @RequestParam String role
    ) {
        LOGGER.info("[registerUser] Performing user registration. email : {}, password : ****, phoneNumber : {}, schoolName : {}, userType : {}", email, phoneNumber, schoolName, userType);
        SignUpResultDto signUpResultDto = signService.signUp(email, password, nickname, phoneNumber, schoolName, userType, role);

        LOGGER.info("[registerUser] User registration completed. email : {}", email);
        return signUpResultDto;
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Logs in a user with the provided credentials.")
    public ResponseEntity<SignInResultDto> login(@Validated @RequestBody SignInRequest signInRequest) {
        SignInResultDto signInResult = signService.signIn(signInRequest.getEmail(), signInRequest.getPassword());
        return ResponseEntity.ok(signInResult);
    }

    @PostMapping("/password-reset/send")
    @Operation(summary = "Send password reset code", description = "Sends a verification code to the user's email for password reset.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification code sent to email."),
            @ApiResponse(responseCode = "400", description = "Invalid email format."),
            @ApiResponse(responseCode = "404", description = "Email not found in the system.")
    })
    public ResponseEntity<PasswordResetResponse> sendPasswordResetCode(@Validated @RequestBody PasswordResetRequest request) {
        PasswordResetResponse response = authService.sendPasswordResetCode(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exception")
    public void exceptionTest() throws RuntimeException {
        throw new RuntimeException("Access is forbidden.");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> exceptionHandler(RuntimeException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOGGER.error("ExceptionHandler called, {}, {}", e.getCause(), e.getMessage());

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "An error occurred");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }
}