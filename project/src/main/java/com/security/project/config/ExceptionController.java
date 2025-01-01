package com.security.project.config;

import com.security.project.user.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.ok().body(
                new Response.ErrorResponse(400, e.getMessage())
        );
    }
}
