package com.example.springdemo.constant;

/**
 * API Constants for centralized configuration
 */
public final class ApiConstants {

    // API Base Paths
    public static final String API_BASE_PATH = "/api";
    public static final String CALCULATOR_PATH = API_BASE_PATH + "/calculator";
    public static final String AUTH_PATH = API_BASE_PATH + "/auth";

    // Calculator Endpoints
    public static final String ADD_ENDPOINT = "/add";
    public static final String SUBTRACT_ENDPOINT = "/subtract";
    public static final String MULTIPLY_ENDPOINT = "/multiply";
    public static final String DIVIDE_ENDPOINT = "/divide";
    public static final String HEALTH_ENDPOINT = "/health";

    // Auth Endpoints
    public static final String SIGNUP_ENDPOINT = "/signup";
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String REFRESH_ENDPOINT = "/refresh";
    public static final String FORGOT_PASSWORD_ENDPOINT = "/forgot-password";
    public static final String RESET_PASSWORD_ENDPOINT = "/reset-password";

    // HTTP Status Messages
    public static final String SUCCESS_MESSAGE = "Operation completed successfully";
    public static final String VALIDATION_ERROR = "Validation failed";
    public static final String INTERNAL_ERROR = "Internal server error";
    public static final String NOT_FOUND_ERROR = "Resource not found";
    public static final String BAD_REQUEST_ERROR = "Bad request";

    // Calculator Messages
    public static final String ADDITION_SUCCESS = "Addition completed successfully";
    public static final String SUBTRACTION_SUCCESS = "Subtraction completed successfully";
    public static final String MULTIPLICATION_SUCCESS = "Multiplication completed successfully";
    public static final String DIVISION_SUCCESS = "Division completed successfully";
    public static final String DIVISION_BY_ZERO = "Division by zero is not allowed";
    public static final String CALCULATOR_HEALTH = "Calculator API is running!";

    // Auth Messages
    public static final String USER_REGISTERED = "User registered successfully";
    public static final String USERNAME_EXISTS = "Username already exists";
    public static final String EMAIL_EXISTS = "Email already exists";
    public static final String REGISTRATION_FAILED = "Registration failed";
    public static final String LOGIN_SUCCESSFUL = "Login successful";
    public static final String LOGIN_FAILED = "Login failed";
    public static final String INVALID_CREDENTIALS = "Invalid username/email or password";
    public static final String ACCOUNT_DISABLED = "Account is disabled";
    public static final String TOKEN_REFRESHED = "Token refreshed successfully";
    public static final String TOKEN_REFRESH_FAILED = "Token refresh failed";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";
    public static final String FORGOT_PASSWORD_SUCCESS = "If the email exists, a password reset link has been sent";
    public static final String FORGOT_PASSWORD_FAILED = "Failed to send password reset email";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successfully";
    public static final String PASSWORD_RESET_FAILED = "Password reset failed";
    public static final String INVALID_RESET_TOKEN = "Invalid or expired reset token";
    public static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
    public static final String RESET_TOKEN_EXPIRED = "Reset token has expired";
    public static final String AUTH_HEALTH = "Authentication API is running!";

    // Response Status
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_ERROR = "error";

    // HTTP Status Codes
    public static final int HTTP_OK = 200;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_INTERNAL_ERROR = 500;

    // Cache Keys
    public static final String CALCULATION_CACHE = "calculation";
    public static final String USER_CACHE = "user";

    // Async Configuration
    public static final String ASYNC_EXECUTOR = "taskExecutor";
    public static final int CORE_POOL_SIZE = 8;
    public static final int MAX_POOL_SIZE = 16;
    public static final int QUEUE_CAPACITY = 100;

    // Performance Thresholds
    public static final long SLOW_OPERATION_THRESHOLD = 1000; // milliseconds

    // Security Constants
    public static final String JWT_PREFIX = "Bearer ";
    public static final String JWT_HEADER = "Authorization";
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    // Validation Messages
    public static final String FIELD_REQUIRED = "Field is required";
    public static final String INVALID_EMAIL = "Invalid email format";
    public static final String PASSWORD_TOO_SHORT = "Password must be at least 6 characters";
    public static final String USERNAME_TOO_SHORT = "Username must be at least 3 characters";

    // Logging Messages
    public static final String REQUEST_LOG_FORMAT = "Request: {} | IP: {} | User-Agent: {} | Data: {}";
    public static final String RESPONSE_LOG_FORMAT = "Response: {} | Data: {}";
    public static final String ERROR_LOG_FORMAT = "Error in {} | IP: {} | Error: {} | Exception: {}";

    // Metadata Keys
    public static final String PROCESSING_TIME = "processingTime";
    public static final String CACHE_STATUS = "cacheStatus";
    public static final String REQUEST_ID = "requestId";

    // Private constructor to prevent instantiation
    private ApiConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
} 