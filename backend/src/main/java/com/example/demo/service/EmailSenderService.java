package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender emailSender;
    private final Map<String, String> validationCodes = new HashMap<>();
    @Autowired
    public EmailSenderService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public String sendAuthenticationEmail(String recipientEmail) {
        String uuid = UUID.randomUUID().toString();
        validationCodes.put(uuid, recipientEmail); // Lưu uuid và email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Xác thực tài khoản gmail");
        message.setText("Xin chào,\n\n Đây là mã xác thực của bạn: \n" + uuid);
        emailSender.send(message);
        System.out.println("Mail Sent successfully");
        return uuid;
    }
    public boolean validateCode(String uuid) {
        System.out.println("Validating code: " + uuid);
        System.out.println("Current validation codes: " + validationCodes);
        boolean isValid = validationCodes.containsKey(uuid);
        if (isValid) {
            validationCodes.remove(uuid); // Xóa mã sau khi xác thực thành công
            System.out.println("Code validated and removed: " + uuid);
        }
        return isValid;
    }
}

