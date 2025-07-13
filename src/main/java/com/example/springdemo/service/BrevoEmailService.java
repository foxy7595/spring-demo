package com.example.springdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Production EmailService implementation using Brevo (Sendinblue) REST API
 * This service is only active when the 'production' profile is enabled
 */
@Service
@Profile("production")
public class BrevoEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(BrevoEmailService.class);
    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email:noreply@example.com}")
    private String senderEmail;

    @Value("${brevo.sender.name:Spring Demo}")
    private String senderName;

    private final WebClient webClient;

    public BrevoEmailService() {
        this.webClient = WebClient.builder()
                .baseUrl(BREVO_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public boolean sendPasswordResetEmail(String toEmail, String username, String resetToken, String resetUrl) {
        try {
            String subject = "Password Reset Request";
            String htmlContent = createPasswordResetEmailHtml(username, resetToken, resetUrl);
            String textContent = createPasswordResetEmailText(username, resetToken, resetUrl);

            Map<String, Object> emailRequest = Map.of(
                "sender", Map.of("email", senderEmail, "name", senderName),
                "to", List.of(Map.of("email", toEmail)),
                "subject", subject,
                "htmlContent", htmlContent,
                "textContent", textContent
            );

            Mono<String> response = webClient.post()
                    .header("api-key", brevoApiKey)
                    .bodyValue(emailRequest)
                    .retrieve()
                    .bodyToMono(String.class);

            response.block(); // Block for synchronous operation
            logger.info("Password reset email sent successfully to: {}", toEmail);
            return true;

        } catch (Exception e) {
            logger.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendWelcomeEmail(String toEmail, String username) {
        try {
            String subject = "Welcome to Spring Demo!";
            String htmlContent = createWelcomeEmailHtml(username);
            String textContent = createWelcomeEmailText(username);

            Map<String, Object> emailRequest = Map.of(
                "sender", Map.of("email", senderEmail, "name", senderName),
                "to", List.of(Map.of("email", toEmail)),
                "subject", subject,
                "htmlContent", htmlContent,
                "textContent", textContent
            );

            Mono<String> response = webClient.post()
                    .header("api-key", brevoApiKey)
                    .bodyValue(emailRequest)
                    .retrieve()
                    .bodyToMono(String.class);

            response.block(); // Block for synchronous operation
            logger.info("Welcome email sent successfully to: {}", toEmail);
            return true;

        } catch (Exception e) {
            logger.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Create HTML content for password reset email
     */
    private String createPasswordResetEmailHtml(String username, String resetToken, String resetUrl) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <title>Password Reset Request</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #f8f9fa; padding: 20px; text-align: center; border-radius: 5px; }
                    .content { padding: 20px; }
                    .button { display: inline-block; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; font-size: 12px; color: #666; }
                    .token { background-color: #f8f9fa; padding: 10px; border-radius: 3px; font-family: monospace; word-break: break-all; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>Password Reset Request</h2>
                    </div>
                    <div class="content">
                        <p>Hello <strong>%s</strong>,</p>
                        <p>We received a request to reset your password. If you didn't make this request, you can safely ignore this email.</p>
                        <p>To reset your password, click the button below:</p>
                        <p style="text-align: center;">
                            <a href="%s" class="button">Reset Password</a>
                        </p>
                        <p>Or copy and paste this link into your browser:</p>
                        <div class="token">%s</div>
                        <p><strong>This link will expire in 1 hour for security reasons.</strong></p>
                        <p>If you have any questions, please contact our support team.</p>
                    </div>
                    <div class="footer">
                        <p>This is an automated message, please do not reply to this email.</p>
                        <p>&copy; 2024 Spring Demo. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, username, resetUrl, resetUrl);
    }

    /**
     * Create text content for password reset email
     */
    private String createPasswordResetEmailText(String username, String resetToken, String resetUrl) {
        return String.format("""
            Password Reset Request
            
            Hello %s,
            
            We received a request to reset your password. If you didn't make this request, you can safely ignore this email.
            
            To reset your password, visit the following link:
            %s
            
            This link will expire in 1 hour for security reasons.
            
            If you have any questions, please contact our support team.
            
            This is an automated message, please do not reply to this email.
            
            © 2024 Spring Demo. All rights reserved.
            """, username, resetUrl);
    }

    /**
     * Create HTML content for welcome email
     */
    private String createWelcomeEmailHtml(String username) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <title>Welcome to Spring Demo!</title>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #28a745; color: white; padding: 20px; text-align: center; border-radius: 5px; }
                    .content { padding: 20px; }
                    .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; font-size: 12px; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>Welcome to Spring Demo!</h2>
                    </div>
                    <div class="content">
                        <p>Hello <strong>%s</strong>,</p>
                        <p>Welcome to Spring Demo! Your account has been successfully created.</p>
                        <p>You can now:</p>
                        <ul>
                            <li>Log in to your account</li>
                            <li>Use our calculator API</li>
                            <li>Manage your profile</li>
                        </ul>
                        <p>If you have any questions, feel free to contact our support team.</p>
                        <p>Thank you for choosing Spring Demo!</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2024 Spring Demo. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """, username);
    }

    /**
     * Create text content for welcome email
     */
    private String createWelcomeEmailText(String username) {
        return String.format("""
            Welcome to Spring Demo!
            
            Hello %s,
            
            Welcome to Spring Demo! Your account has been successfully created.
            
            You can now:
            - Log in to your account
            - Use our calculator API
            - Manage your profile
            
            If you have any questions, feel free to contact our support team.
            
            Thank you for choosing Spring Demo!
            
            © 2024 Spring Demo. All rights reserved.
            """, username);
    }
} 