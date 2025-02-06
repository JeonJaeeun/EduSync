package org.edusync.global.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {
    
    @RequestMapping("/error")
    public ResponseEntity<ErrorResponse> handleError() {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("An error occurred"));
    }
} 