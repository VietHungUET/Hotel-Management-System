package com.example.demo.service;

import com.example.demo.agent.HotelAssistant;
import com.example.demo.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final HotelAssistant hotelAssistant;

    public ChatResponse processMessage(String message, Integer userId) {
        log.info("Processing message from user {}: {}", userId, message);
        
        try {
            String response = hotelAssistant.chat(message, userId);
            
            return new ChatResponse(
                response,
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                null
            );
        } catch (Exception e) {
            log.error("Error processing message: ", e);
            return new ChatResponse(
                "Xin lỗi, tôi gặp lỗi khi xử lý câu hỏi của bạn. Vui lòng thử lại.",
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                null
            );
        }
    }
}