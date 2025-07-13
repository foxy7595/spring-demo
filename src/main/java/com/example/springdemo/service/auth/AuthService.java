package com.example.springdemo.service.auth;

import com.example.springdemo.model.auth.AuthResponse;
import com.example.springdemo.model.auth.SignupRequest;
import com.example.springdemo.model.auth.User;
import com.example.springdemo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(JwtService jwtService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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

            // Generate JWT token
            String token = jwtService.generateTokenForUser(savedUser.getUsername());

            logger.info("User registered successfully: {}", savedUser.getUsername());

            return new AuthResponse(
                token,
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
} 