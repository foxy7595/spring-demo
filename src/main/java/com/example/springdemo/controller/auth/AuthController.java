package com.example.springdemo.controller.auth;

import com.example.springdemo.model.auth.AuthResponse;
import com.example.springdemo.model.auth.ForgotPasswordRequest;
import com.example.springdemo.model.auth.LoginRequest;
import com.example.springdemo.model.auth.RefreshTokenRequest;
import com.example.springdemo.model.auth.ResetPasswordRequest;
import com.example.springdemo.model.auth.SignupRequest;
import com.example.springdemo.model.common.ApiResponse;
import com.example.springdemo.service.auth.AuthService;
import com.example.springdemo.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "User authentication and authorization endpoints")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the provided information and returns a JWT token and refresh token"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User registered successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"User registered successfully\", \"data\": {\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"refreshToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"type\": \"Bearer\", \"username\": \"john_doe\", \"email\": \"john@example.com\", \"fullName\": \"John Doe\", \"message\": \"User registered successfully\"}, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/signup\"}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - Validation error or user already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Error Response",
                    value = "{\"status\": \"error\", \"code\": 400, \"message\": \"Username already exists\", \"errors\": [\"Username already exists\"], \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/signup\"}"
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> signup(
            @Parameter(
                description = "User registration information",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Signup Request",
                        value = "{\"username\": \"john_doe\", \"email\": \"john@example.com\", \"password\": \"password123\", \"fullName\": \"John Doe\"}"
                    )
                )
            )
            @Valid @RequestBody SignupRequest signupRequest) {
        try {
            AuthResponse response = authService.signup(signupRequest);
            if (response.getToken() != null) {
                return ResponseUtil.success(response, "User registered successfully", "/api/auth/signup");
            } else {
                return ResponseUtil.badRequest(response.getMessage(), "/api/auth/signup");
            }
        } catch (Exception e) {
            return ResponseUtil.badRequest("Registration failed: " + e.getMessage(), "/api/auth/signup");
        }
    }

    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticates a user with username/email and password and returns a JWT token and refresh token"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"Login successful\", \"data\": {\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"refreshToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"type\": \"Bearer\", \"username\": \"john_doe\", \"email\": \"john@example.com\", \"fullName\": \"John Doe\", \"message\": \"Login successful\"}, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/login\"}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid credentials or account disabled",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Error Response",
                    value = "{\"status\": \"error\", \"code\": 400, \"message\": \"Invalid username/email or password\", \"errors\": [\"Invalid username/email or password\"], \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/login\"}"
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Parameter(
                description = "User login credentials",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Login Request",
                        value = "{\"usernameOrEmail\": \"john_doe\", \"password\": \"password123\"}"
                    )
                )
            )
            @Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            if (response.getToken() != null) {
                return ResponseUtil.success(response, "Login successful", "/api/auth/login");
            } else {
                return ResponseUtil.badRequest(response.getMessage(), "/api/auth/login");
            }
        } catch (Exception e) {
            return ResponseUtil.badRequest("Login failed: " + e.getMessage(), "/api/auth/login");
        }
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "Refresh JWT token",
        description = "Refreshes the JWT token using a valid refresh token and returns a new JWT token and refresh token"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Token refreshed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"Token refreshed successfully\", \"data\": {\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"refreshToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"type\": \"Bearer\", \"username\": \"john_doe\", \"email\": \"john@example.com\", \"fullName\": \"John Doe\", \"message\": \"Token refreshed successfully\"}, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/refresh\"}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid refresh token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Error Response",
                    value = "{\"status\": \"error\", \"code\": 400, \"message\": \"Invalid refresh token\", \"errors\": [\"Invalid refresh token\"], \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/refresh\"}"
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Parameter(
                description = "Refresh token request",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Refresh Token Request",
                        value = "{\"refreshToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"
                    )
                )
            )
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            AuthResponse response = authService.refreshToken(refreshTokenRequest);
            if (response.getToken() != null) {
                return ResponseUtil.success(response, "Token refreshed successfully", "/api/auth/refresh");
            } else {
                return ResponseUtil.badRequest(response.getMessage(), "/api/auth/refresh");
            }
        } catch (Exception e) {
            return ResponseUtil.badRequest("Token refresh failed: " + e.getMessage(), "/api/auth/refresh");
        }
    }

    @PostMapping("/forgot-password")
    @Operation(
        summary = "Forgot password",
        description = "Sends a password reset email to the provided email address"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Password reset email sent",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"If the email exists, a password reset link has been sent\", \"data\": {\"message\": \"If the email exists, a password reset link has been sent\"}, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/forgot-password\"}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid email or email sending failed",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Error Response",
                    value = "{\"status\": \"error\", \"code\": 400, \"message\": \"Failed to send password reset email\", \"errors\": [\"Failed to send password reset email\"], \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/forgot-password\"}"
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> forgotPassword(
            @Parameter(
                description = "Forgot password request",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Forgot Password Request",
                        value = "{\"email\": \"user@example.com\"}"
                    )
                )
            )
            @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            AuthResponse response = authService.forgotPassword(forgotPasswordRequest);
            return ResponseUtil.success(response, response.getMessage(), "/api/auth/forgot-password");
        } catch (Exception e) {
            return ResponseUtil.badRequest("Forgot password failed: " + e.getMessage(), "/api/auth/forgot-password");
        }
    }

    @PostMapping("/reset-password")
    @Operation(
        summary = "Reset password",
        description = "Resets the user password using a valid reset token"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Password reset successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"Password reset successfully\", \"data\": {\"username\": \"john_doe\", \"email\": \"john@example.com\", \"fullName\": \"John Doe\", \"message\": \"Password reset successfully\"}, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/reset-password\"}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid token or passwords do not match",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Error Response",
                    value = "{\"status\": \"error\", \"code\": 400, \"message\": \"Invalid or expired reset token\", \"errors\": [\"Invalid or expired reset token\"], \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/reset-password\"}"
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> resetPassword(
            @Parameter(
                description = "Reset password request",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Reset Password Request",
                        value = "{\"resetToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"newPassword\": \"newPassword123\", \"confirmPassword\": \"newPassword123\"}"
                    )
                )
            )
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            AuthResponse response = authService.resetPassword(resetPasswordRequest);
            if (response.getMessage() != null && response.getMessage().contains("successfully")) {
                return ResponseUtil.success(response, "Password reset successfully", "/api/auth/reset-password");
            } else {
                return ResponseUtil.badRequest(response.getMessage(), "/api/auth/reset-password");
            }
        } catch (Exception e) {
            return ResponseUtil.badRequest("Password reset failed: " + e.getMessage(), "/api/auth/reset-password");
        }
    }

    @GetMapping("/health")
    @Operation(
        summary = "Health check",
        description = "Returns the health status of the authentication API"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "API is healthy",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Health Response",
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"Authentication API is running!\", \"data\": null, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/health\"}"
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseUtil.success("Authentication API is running!", "/api/auth/health");
    }
} 