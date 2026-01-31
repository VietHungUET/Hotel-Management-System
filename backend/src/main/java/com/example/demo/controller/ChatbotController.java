package com.example.demo.controller;

import com.example.demo.request.ChatRequest;
import com.example.demo.response.ChatResponse;
import com.example.demo.service.ChatbotService;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.CustomDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse> chat(
            @RequestBody ChatRequest request,
            @AuthenticationPrincipal CustomDetails userDetails) {
        
        int userId = userDetails.getUser().getId();
        
        ChatResponse response = chatbotService.processMessage(
            request.getMessage(), 
            userId
        );
        
        return ResponseEntity.ok(new ApiResponse("Success", response));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> health() {
        return ResponseEntity.ok(new ApiResponse("Chatbot is running", null));
    }
}