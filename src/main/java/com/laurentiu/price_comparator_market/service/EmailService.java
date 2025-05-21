package com.laurentiu.price_comparator_market.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @PostConstruct
    public void checkEmailConfig() {
        log.info("Checking email configuration:");
        log.info("Username configured: {}", System.getenv("MAIL_USERNAME") != null ? "Yes" : "No");
        log.info("Password configured: {}", System.getenv("MAIL_PASSWORD") != null ? "Yes" : "No");
    }

    public void sendPriceAlert(String to, String productName, BigDecimal currentPrice,
                               BigDecimal targetPrice, String supermarketName) {
        log.info("Starting to send email alert...");
        log.info("To: {}, Product: {}, Price: {}, Target: {}, Store: {}",
                to, productName, currentPrice, targetPrice, supermarketName);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Price alert for " + productName);
            String text = "Hi,\n\n" +
                    "The price of " + productName + " at " + supermarketName.toUpperCase() + " is now " + currentPrice +
                    " and your target price is " + targetPrice + ".\n\n" +
                    "Best regards,\n" +
                    "Price Comparator Market";
            message.setText(text);

            log.info("Attempting to send email...");
            mailSender.send(message);
            log.info("Email sent successfully!");
        } catch (Exception e) {
            log.error("Failed to send email. Error: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }

}
