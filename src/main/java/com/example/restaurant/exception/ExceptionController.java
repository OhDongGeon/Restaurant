package com.example.restaurant.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(FindException.class)
    public ResponseEntity<ExceptionResponse> memberRequestException(final FindException findException) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(findException.getMessage(), findException.getErrorCode()));
    }


    @Getter
    @AllArgsConstructor
    public static class ExceptionResponse {
        private String message;
        private ErrorCode errorCode;
    }
}
