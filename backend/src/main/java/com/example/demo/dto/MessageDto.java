package com.example.demo.dto;

import java.time.LocalDateTime;

public record MessageDto(
        String role, // "user" or "assistant"
        String content,
        LocalDateTime timestamp
) {}