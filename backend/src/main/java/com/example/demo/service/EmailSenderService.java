package com.example.demo.service;

import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender emailSender;
    private final RegistrationCodeStore registrationCodeStore;

    public EmailSenderService(JavaMailSender emailSender, RegistrationCodeStore registrationCodeStore) {
        this.emailSender = emailSender;
        this.registrationCodeStore = registrationCodeStore;
    }

    public String sendAuthenticationEmail(String recipientEmail) {
        try {
            String code = UUID.randomUUID().toString();
            registrationCodeStore.save(code, recipientEmail);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Xác thực tài khoản");
            message.setText("Xin chào,\n\nMã xác thực của bạn là:\n" + code);
            emailSender.send(message);

            return code;
        } catch (Exception e) {
            throw new RuntimeException("Unable to send registration email: " + e.getMessage(), e);
        }
    }

    public boolean validateCode(String code) {
        return registrationCodeStore.consume(code);
    }
}
