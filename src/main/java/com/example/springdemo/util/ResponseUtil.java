package com.example.springdemo.util;

import com.example.springdemo.constant.ApiConstants;
import com.example.springdemo.model.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Utility class for building standardized API responses
 */
public final class ResponseUtil {

    private ResponseUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Build a successful response with data
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message, String path) {
        ApiResponse<T> response = ApiResponse.success(data, message, path);
        return ResponseEntity.ok(response);
    }

    /**
     * Build a successful response with data and default message
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String path) {
        return success(data, ApiConstants.SUCCESS_MESSAGE, path);
    }

    /**
     * Build a successful response with message only
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, String path) {
        ApiResponse<T> response = ApiResponse.success(message, path);
        return ResponseEntity.ok(response);
    }

    /**
     * Build an error response
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message, String path) {
        ApiResponse<T> response = ApiResponse.error(status.value(), message, path);
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Build an error response with multiple error messages
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message, List<String> errors, String path) {
        ApiResponse<T> response = ApiResponse.error(status.value(), message, errors, path);
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Build a bad request response
     */
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message, String path) {
        return error(HttpStatus.BAD_REQUEST, message, path);
    }

    /**
     * Build a not found response
     */
    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message, String path) {
        return error(HttpStatus.NOT_FOUND, message, path);
    }

    /**
     * Build an internal server error response
     */
    public static <T> ResponseEntity<ApiResponse<T>> internalError(String message, String path) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message, path);
    }

    /**
     * Build a response with metadata
     */
    public static <T> ResponseEntity<ApiResponse<T>> successWithMetadata(T data, String message, String path, 
                                                                        Long processingTime, String cacheStatus) {
        ApiResponse<T> response = ApiResponse.success(data, message, path);
        
        ApiResponse.Metadata metadata = new ApiResponse.Metadata(processingTime, cacheStatus);
        response.setMetadata(metadata);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Build a response with pagination metadata
     */
    public static <T> ResponseEntity<ApiResponse<T>> successWithPagination(T data, String message, String path,
                                                                          Long total, Integer page, Integer size) {
        ApiResponse<T> response = ApiResponse.success(data, message, path);
        
        ApiResponse.Metadata metadata = new ApiResponse.Metadata(total, page, size);
        response.setMetadata(metadata);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Build a validation error response
     */
    public static <T> ResponseEntity<ApiResponse<T>> validationError(List<String> errors, String path) {
        return error(HttpStatus.BAD_REQUEST, ApiConstants.VALIDATION_ERROR, errors, path);
    }

    /**
     * Build a resource not found response
     */
    public static <T> ResponseEntity<ApiResponse<T>> resourceNotFound(String resourceName, String path) {
        String message = String.format("%s not found", resourceName);
        return notFound(message, path);
    }

    /**
     * Build an unauthorized response
     */
    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message, String path) {
        return error(HttpStatus.UNAUTHORIZED, message, path);
    }

    /**
     * Build a forbidden response
     */
    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message, String path) {
        return error(HttpStatus.FORBIDDEN, message, path);
    }

    /**
     * Build a conflict response (e.g., duplicate resource)
     */
    public static <T> ResponseEntity<ApiResponse<T>> conflict(String message, String path) {
        return error(HttpStatus.CONFLICT, message, path);
    }

    /**
     * Build a service unavailable response
     */
    public static <T> ResponseEntity<ApiResponse<T>> serviceUnavailable(String message, String path) {
        return error(HttpStatus.SERVICE_UNAVAILABLE, message, path);
    }

    /**
     * Calculate processing time in milliseconds
     */
    public static long calculateProcessingTime(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Determine cache status message
     */
    public static String getCacheStatus(boolean fromCache) {
        return fromCache ? "HIT" : "MISS";
    }
} 