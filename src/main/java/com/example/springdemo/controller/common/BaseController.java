package com.example.springdemo.controller.common;

import com.example.springdemo.model.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Base controller providing common functionality for all controllers
 */
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Create a successful response with data
     */
    protected <T> ApiResponse<T> successResponse(T data, String message) {
        String path = getCurrentRequestPath();
        return ApiResponse.success(data, message, path);
    }

    /**
     * Create a successful response with data and default message
     */
    protected <T> ApiResponse<T> successResponse(T data) {
        String path = getCurrentRequestPath();
        return ApiResponse.success(data, path);
    }

    /**
     * Create a successful response with message only
     */
    protected <T> ApiResponse<T> successResponse(String message) {
        String path = getCurrentRequestPath();
        return ApiResponse.success(message, path);
    }

    /**
     * Create an error response
     */
    protected <T> ApiResponse<T> errorResponse(int code, String message) {
        String path = getCurrentRequestPath();
        return ApiResponse.error(code, message, path);
    }

    /**
     * Create an error response with multiple error messages
     */
    protected <T> ApiResponse<T> errorResponse(int code, String message, List<String> errors) {
        String path = getCurrentRequestPath();
        return ApiResponse.error(code, message, errors, path);
    }

    /**
     * Create a bad request response
     */
    protected <T> ApiResponse<T> badRequestResponse(String message) {
        String path = getCurrentRequestPath();
        return ApiResponse.badRequest(message, path);
    }

    /**
     * Create a not found response
     */
    protected <T> ApiResponse<T> notFoundResponse(String message) {
        String path = getCurrentRequestPath();
        return ApiResponse.notFound(message, path);
    }

    /**
     * Create an internal server error response
     */
    protected <T> ApiResponse<T> internalErrorResponse(String message) {
        String path = getCurrentRequestPath();
        return ApiResponse.internalError(message, path);
    }

    /**
     * Get the current request path
     */
    protected String getCurrentRequestPath() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getRequestURI();
            }
        } catch (Exception e) {
            logger.warn("Could not get current request path: {}", e.getMessage());
        }
        return "/unknown";
    }

    /**
     * Get client IP address from request
     */
    protected String getClientIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            logger.warn("Could not get client IP address: {}", e.getMessage());
        }
        return "unknown";
    }

    /**
     * Get user agent from request
     */
    protected String getUserAgent() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getHeader("User-Agent");
            }
        } catch (Exception e) {
            logger.warn("Could not get user agent: {}", e.getMessage());
        }
        return "unknown";
    }

    /**
     * Log request information
     */
    protected void logRequest(String operation, Object requestData) {
        String clientIp = getClientIpAddress();
        String userAgent = getUserAgent();
        logger.info("Request: {} | IP: {} | User-Agent: {} | Data: {}", 
            operation, clientIp, userAgent, requestData);
    }

    /**
     * Log response information
     */
    protected void logResponse(String operation, Object responseData) {
        logger.info("Response: {} | Data: {}", operation, responseData);
    }

    /**
     * Log error information
     */
    protected void logError(String operation, String errorMessage, Exception e) {
        String clientIp = getClientIpAddress();
        logger.error("Error in {} | IP: {} | Error: {} | Exception: {}", 
            operation, clientIp, errorMessage, e.getMessage(), e);
    }
} 