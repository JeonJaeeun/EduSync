package org.edusync.tutor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.edusync.tutor.dto.FcmRequest;
import org.edusync.tutor.dto.FcmResponse;
import org.edusync.tutor.service.FirebaseMessagingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fcm")
@Tag(name = "FCM Controller", description = "FCM Token and Notification Management")
public class FcmController {

    private final FirebaseMessagingService firebaseMessagingService;

    public FcmController(FirebaseMessagingService firebaseMessagingService) {
        this.firebaseMessagingService = firebaseMessagingService;
    }

    @Operation(summary = "Save or update FCM token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FCM token saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/token")
    public ResponseEntity<FcmResponse> saveOrUpdateToken(@RequestBody FcmRequest request) {
        String userEmail = request.getUserEmail();
        String token = request.getBody();

        if (userEmail == null || token == null) {
            return ResponseEntity.badRequest().body(new FcmResponse("userId and token are required."));
        }

        firebaseMessagingService.saveOrUpdateToken(userEmail, token);
        return ResponseEntity.ok(new FcmResponse("FCM token saved successfully."));
    }

    @Operation(summary = "Send notification to a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/send/user")
    public ResponseEntity<FcmResponse> sendNotificationToUser(@RequestBody FcmRequest request) {
        String userId = request.getUserEmail();
        String title = request.getTitle();
        String body = request.getBody();

        if (userId == null || title == null || body == null) {
            return ResponseEntity.badRequest().body(new FcmResponse("userId, title, and body are required."));
        }

        firebaseMessagingService.sendNotificationToUser(userId, title, body);
        return ResponseEntity.ok(new FcmResponse("Notification sent successfully."));
    }

    @Operation(summary = "Send notification to multiple users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/send/multiple")
    public ResponseEntity<FcmResponse> sendNotificationToMultipleUsers(@RequestBody FcmRequest request) {
        List<String> userIds = request.getUserEmails();
        String title = request.getTitle();
        String body = request.getBody();

        if (userIds == null || userIds.isEmpty() || title == null || body == null) {
            return ResponseEntity.badRequest().body(new FcmResponse("userIds, title, and body are required."));
        }

        firebaseMessagingService.sendNotificationToMultipleUsers(userIds, title, body);
        return ResponseEntity.ok(new FcmResponse("Notifications sent successfully."));
    }
}