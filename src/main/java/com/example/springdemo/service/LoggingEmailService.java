package com.example.springdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Development EmailService implementation that logs emails instead of sending them
 * This service is active by default (no specific profile required)
 */
@Service
@Profile("!production")
public class LoggingEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(LoggingEmailService.class);

    @Value("${brevo.sender.email:noreply@example.com}")
    private String senderEmail;

    @Value("${brevo.sender.name:Spring Demo}")
    private String senderName;

    @Override
    public boolean sendPasswordResetEmail(String toEmail, String username, String resetToken, String resetUrl) {
        try {
            logger.info("=== PASSWORD RESET EMAIL ===");
            logger.info("To: {}", toEmail);
            logger.info("From: {} <{}>", senderName, senderEmail);
            logger.info("Subject: Password Reset Request");
            logger.info("Username: {}", username);
            logger.info("Reset Token: {}", resetToken);
            logger.info("Reset URL: {}", resetUrl);
            logger.info("=== END EMAIL ===");
            
            // In a real implementation, this would send via Brevo
            // For now, we just log the email content
            return true;

        } catch (Exception e) {
            logger.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendWelcomeEmail(String toEmail, String username) {
        try {
            logger.info("=== WELCOME EMAIL ===");
            logger.info("To: {}", toEmail);
            logger.info("From: {} <{}>", senderName, senderEmail);
            logger.info("Subject: Welcome to Spring Demo!");
            logger.info("Username: {}", username);
            logger.info("=== END EMAIL ===");
            
            // In a real implementation, this would send via Brevo
            // For now, we just log the email content
            return true;

        } catch (Exception e) {
            logger.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage(), e);
            return false;
        }
    }
} 