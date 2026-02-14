package com.okayji.exception;

import com.okayji.common.ApiResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j(topic = "GLOBAL-EXCEPTION-HANDLER")
public class GlobalExceptionHandler {
    /*
     * Unhandled errors during runtime
     */
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                        .success(false)
                        .message(AppError.UNCATEGORIZED_EXCEPTION.getMessage())
                        .build());
    }

    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatusCode())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(ex.getErrorCode().getMessage())
                        .build());
    }

    /*
     * Exception from annotations
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getFieldError().getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .success(false)
                        .message(message)
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        AppError appError = AppError.UNAUTHORIZED;

        return ResponseEntity.status(appError.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(appError.getMessage())
                        .build());
    }

    @ExceptionHandler(JwtException.class)
    ResponseEntity<ApiResponse> handleJwtException(JwtException ex) {
        AppError appError = AppError.UNAUTHENTICATED;

        return ResponseEntity.status(appError.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(appError.getMessage())
                        .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ApiResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Throwable cause = ex.getCause();
        AppError appError = AppError.UNCATEGORIZED_EXCEPTION;

        if (cause instanceof ConstraintViolationException cve) {
            String constraint = cve.getConstraintName();

            switch(constraint) {
                case "user.uk_user_username" -> appError = AppError.USERNAME_EXISTED;
                case "user.uk_user_email" -> appError = AppError.EMAIL_EXISTED;
                case "friend_request.uk_fr_pair" -> appError = AppError.FRIEND_REQUEST_EXISTS;
                case "friend.uk_friends_pair" -> appError = AppError.FRIEND_ALREADY;
            }
        }
        return ResponseEntity.status(appError.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(appError.getMessage())
                        .build());
    }

    /*
     * Issues with the request body
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        AppError appError = AppError.INVALID_INPUT_DATA;
        return ResponseEntity.status(appError.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(appError.getMessage())
                        .build());
    }

}
