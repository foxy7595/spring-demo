package com.example.springdemo.controller.auth;

import com.example.springdemo.model.auth.AuthResponse;
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
        description = "Creates a new user account with the provided information and returns a JWT token"
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
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"User registered successfully\", \"data\": {\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"type\": \"Bearer\", \"username\": \"john_doe\", \"email\": \"john@example.com\", \"fullName\": \"John Doe\", \"message\": \"User registered successfully\"}, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/auth/signup\"}"
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