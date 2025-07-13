package com.example.springdemo.service.auth;

import com.example.springdemo.model.auth.AuthResponse;
import com.example.springdemo.model.auth.ForgotPasswordRequest;
import com.example.springdemo.model.auth.LoginRequest;
import com.example.springdemo.model.auth.RefreshTokenRequest;
import com.example.springdemo.model.auth.ResetPasswordRequest;
import com.example.springdemo.model.auth.SignupRequest;
import com.example.springdemo.model.auth.User;
import com.example.springdemo.repository.UserRepository;
import com.example.springdemo.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.password-reset.url:http://localhost:3000/reset-password}")
    private String passwordResetUrl;

    @Autowired
    public AuthService(JwtService jwtService, PasswordEncoder passwordEncoder, UserRepository userRepository, EmailService emailService) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public AuthResponse signup(SignupRequest signupRequest) {
        logger.info("Processing signup request for username: {}", signupRequest.getUsername());

        // Check if username already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            logger.warn("Username already exists: {}", signupRequest.getUsername());
            return new AuthResponse("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            logger.warn("Email already exists: {}", signupRequest.getEmail());
            return new AuthResponse("Email already exists");
        }

        try {
            // Create new user
            User user = new User();
            user.setUsername(signupRequest.getUsername());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            user.setFullName(signupRequest.getFullName());
            user.setRole("USER");
            user.setEnabled(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            // Save user to MongoDB
            User savedUser = userRepository.save(user);

            // Generate JWT token and refresh token
            String token = jwtService.generateTokenForUser(savedUser.getUsername());
            String refreshToken = jwtService.generateRefreshToken(savedUser);

            // Save refresh token to user
            savedUser.setRefreshToken(refreshToken);
            userRepository.save(savedUser);

            // Send welcome email (async)
            try {
                emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getUsername());
            } catch (Exception e) {
                logger.warn("Failed to send welcome email to {}: {}", savedUser.getEmail(), e.getMessage());
            }

            logger.info("User registered successfully: {}", savedUser.getUsername());

            return new AuthResponse(
                token,
                refreshToken,
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                "User registered successfully"
            );

        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage(), e);
            return new AuthResponse("Registration failed: " + e.getMessage());
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        logger.info("Processing login request for: {}", loginRequest.getUsernameOrEmail());

        try {
            // Find user by username or email
            User user = findByUsernameOrEmail(loginRequest.getUsernameOrEmail());
            
            if (user == null) {
                logger.warn("Login failed: User not found - {}", loginRequest.getUsernameOrEmail());
                return new AuthResponse("Invalid username/email or password");
            }

            // Check if user is enabled
            if (!user.isEnabled()) {
                logger.warn("Login failed: User account disabled - {}", user.getUsername());
                return new AuthResponse("Account is disabled");
            }

            // Verify password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                logger.warn("Login failed: Invalid password for user - {}", user.getUsername());
                return new AuthResponse("Invalid username/email or password");
            }

            // Generate JWT token and refresh token
            String token = jwtService.generateTokenForUser(user.getUsername());
            String refreshToken = jwtService.generateRefreshToken(user);

            // Save refresh token to user
            user.setRefreshToken(refreshToken);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            logger.info("User logged in successfully: {}", user.getUsername());

            return new AuthResponse(
                token,
                refreshToken,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                "Login successful"
            );

        } catch (Exception e) {
            logger.error("Error during user login: {}", e.getMessage(), e);
            return new AuthResponse("Login failed: " + e.getMessage());
        }
    }

    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        logger.info("Processing refresh token request");

        try {
            String refreshToken = refreshTokenRequest.getRefreshToken();
            
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                logger.warn("Refresh token is null or empty");
                return new AuthResponse("Invalid refresh token");
            }

            // Extract username from refresh token
            String username = jwtService.extractUsername(refreshToken);
            
            if (username == null) {
                logger.warn("Could not extract username from refresh token");
                return new AuthResponse("Invalid refresh token");
            }

            // Find user by username
            User user = findByUsername(username);
            
            if (user == null) {
                logger.warn("User not found for refresh token: {}", username);
                return new AuthResponse("Invalid refresh token");
            }

            // Check if user is enabled
            if (!user.isEnabled()) {
                logger.warn("User account disabled for refresh token: {}", username);
                return new AuthResponse("Account is disabled");
            }

            // Verify that the stored refresh token matches the provided one
            if (!refreshToken.equals(user.getRefreshToken())) {
                logger.warn("Refresh token mismatch for user: {}", username);
                return new AuthResponse("Invalid refresh token");
            }

            // Verify refresh token is valid
            if (!jwtService.isTokenValid(refreshToken, user)) {
                logger.warn("Invalid refresh token for user: {}", username);
                return new AuthResponse("Invalid refresh token");
            }

            // Generate new JWT token and refresh token
            String newToken = jwtService.generateTokenForUser(user.getUsername());
            String newRefreshToken = jwtService.generateRefreshToken(user);

            // Update user with new refresh token
            user.setRefreshToken(newRefreshToken);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            logger.info("Token refreshed successfully for user: {}", user.getUsername());

            return new AuthResponse(
                newToken,
                newRefreshToken,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                "Token refreshed successfully"
            );

        } catch (Exception e) {
            logger.error("Error during token refresh: {}", e.getMessage(), e);
            return new AuthResponse("Token refresh failed: " + e.getMessage());
        }
    }

    public AuthResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        logger.info("Processing forgot password request for email: {}", forgotPasswordRequest.getEmail());

        try {
            String email = forgotPasswordRequest.getEmail();
            
            // Find user by email
            User user = userRepository.findByEmail(email).orElse(null);
            
            if (user == null) {
                // Don't reveal if email exists or not for security reasons
                logger.info("Forgot password request for non-existent email: {}", email);
                return new AuthResponse("If the email exists, a password reset link has been sent");
            }

            // Check if user is enabled
            if (!user.isEnabled()) {
                logger.warn("Forgot password request for disabled account: {}", email);
                return new AuthResponse("If the email exists, a password reset link has been sent");
            }

            // Generate password reset token
            String resetToken = jwtService.generatePasswordResetToken(user.getEmail());
            
            // Save reset token to user
            user.setPasswordResetToken(resetToken);
            user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1));
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            // Create reset URL
            String resetUrl = passwordResetUrl + "?token=" + resetToken;

            // Send password reset email
            boolean emailSent = emailService.sendPasswordResetEmail(
                user.getEmail(), 
                user.getUsername(), 
                resetToken, 
                resetUrl
            );

            if (emailSent) {
                logger.info("Password reset email sent successfully to: {}", email);
                return new AuthResponse("If the email exists, a password reset link has been sent");
            } else {
                logger.error("Failed to send password reset email to: {}", email);
                return new AuthResponse("Failed to send password reset email. Please try again later.");
            }

        } catch (Exception e) {
            logger.error("Error during forgot password process: {}", e.getMessage(), e);
            return new AuthResponse("An error occurred. Please try again later.");
        }
    }

    public AuthResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        logger.info("Processing password reset request");

        try {
            String resetToken = resetPasswordRequest.getResetToken();
            String newPassword = resetPasswordRequest.getNewPassword();
            String confirmPassword = resetPasswordRequest.getConfirmPassword();

            // Validate passwords match
            if (!newPassword.equals(confirmPassword)) {
                logger.warn("Password reset failed: passwords do not match");
                return new AuthResponse("Passwords do not match");
            }

            // Validate reset token
            if (!jwtService.isPasswordResetTokenValid(resetToken)) {
                logger.warn("Password reset failed: invalid reset token");
                return new AuthResponse("Invalid or expired reset token");
            }

            // Extract email from token
            String email = jwtService.extractEmailFromPasswordResetToken(resetToken);
            if (email == null) {
                logger.warn("Password reset failed: could not extract email from token");
                return new AuthResponse("Invalid reset token");
            }

            // Find user by email
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                logger.warn("Password reset failed: user not found for email: {}", email);
                return new AuthResponse("Invalid reset token");
            }

            // Check if user is enabled
            if (!user.isEnabled()) {
                logger.warn("Password reset failed: user account disabled: {}", email);
                return new AuthResponse("Account is disabled");
            }

            // Verify stored reset token matches
            if (!resetToken.equals(user.getPasswordResetToken())) {
                logger.warn("Password reset failed: token mismatch for user: {}", user.getUsername());
                return new AuthResponse("Invalid reset token");
            }

            // Check if token is expired
            if (user.getPasswordResetTokenExpiry() == null || 
                user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
                logger.warn("Password reset failed: token expired for user: {}", user.getUsername());
                return new AuthResponse("Reset token has expired");
            }

            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setPasswordResetToken(null);
            user.setPasswordResetTokenExpiry(null);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            logger.info("Password reset successfully for user: {}", user.getUsername());

            return new AuthResponse(
                null,
                null,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                "Password reset successfully"
            );

        } catch (Exception e) {
            logger.error("Error during password reset: {}", e.getMessage(), e);
            return new AuthResponse("Password reset failed: " + e.getMessage());
        }
    }

    public User findByUsernameOrEmail(String usernameOrEmail) {
        // First try to find by username
        User user = userRepository.findByUsername(usernameOrEmail).orElse(null);
        
        if (user == null) {
            // If not found by username, try by email
            user = userRepository.findByEmail(usernameOrEmail).orElse(null);
        }
        
        return user;
    }
} 