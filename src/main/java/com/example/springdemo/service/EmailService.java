package com.example.springdemo.service;

/**
 * Email service interface for sending emails
 */
public interface EmailService {

    /**
     * Send password reset email
     * @param toEmail recipient email address
     * @param username username of the recipient
     * @param resetToken password reset token
     * @param resetUrl password reset URL
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendPasswordResetEmail(String toEmail, String username, String resetToken, String resetUrl);

    /**
     * Send welcome email
     * @param toEmail recipient email address
     * @param username username of the recipient
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendWelcomeEmail(String toEmail, String username);
} 