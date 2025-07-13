package com.example.springdemo.exception;

import com.example.springdemo.model.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return fieldName + ": " + errorMessage;
                })
                .collect(Collectors.toList());

        String path = request.getDescription(false).replace("uri=", "");
        ApiResponse<Void> errorResponse = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            errors,
            path
        );

        logger.warn("Validation error: {}", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        String path = request.getDescription(false).replace("uri=", "");
        ApiResponse<Void> errorResponse = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid argument",
            List.of(ex.getMessage()),
            path
        );

        logger.warn("Illegal argument error: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        String path = request.getDescription(false).replace("uri=", "");
        ApiResponse<Void> errorResponse = ApiResponse.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal server error",
            List.of(ex.getMessage()),
            path
        );

        logger.error("Runtime error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex, WebRequest request) {
        
        String path = request.getDescription(false).replace("uri=", "");
        ApiResponse<Void> errorResponse = ApiResponse.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            List.of(ex.getMessage()),
            path
        );

        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 