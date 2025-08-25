package com.example.demo.service;


import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender emailSender;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public EmailSenderService(JavaMailSender emailSender, StringRedisTemplate redisTemplate) {
        this.emailSender = emailSender;
        this.redisTemplate = redisTemplate;
        System.out.println("EmailSenderService initialized - redisTemplate: " + (redisTemplate != null ? "OK" : "NULL"));
    }

    public String sendAuthenticationEmail(String recipientEmail) {
        try {
            String uuid = UUID.randomUUID().toString();
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            // Lưu uuid -> email với TTL 10 phút
            ops.set(uuid, recipientEmail, 10, TimeUnit.MINUTES);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Xác thực tài khoản gmail");
            message.setText("Xin chào,\n\n Đây là mã xác thực của bạn: \n" + uuid);
            emailSender.send(message);
            System.out.println("Mail Sent successfully");
            return uuid;
        } catch (Exception e) {
            System.err.println("Redis error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unable to connect to Redis: " + e.getMessage());
        }
    }

    public boolean validateCode(String uuid) {
        try {
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            String email = ops.get(uuid);
            boolean isValid = (email != null);
            if (isValid) {
                redisTemplate.delete(uuid); // Xóa mã sau khi xác thực thành công
                System.out.println("Code validated and removed: " + uuid);
            }
            return isValid;
        } catch (Exception e) {
            System.err.println("Redis error in validateCode: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

