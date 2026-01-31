package com.example.demo.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDto {
    private String role; // "user" or "assistant"
    private String content;
    private LocalDateTime timestamp;
}