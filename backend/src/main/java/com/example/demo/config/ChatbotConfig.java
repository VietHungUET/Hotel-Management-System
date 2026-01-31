package com.example.demo.config;

import com.example.demo.agent.HotelAssistant;
import com.example.demo.tools.RoomTools;
import com.example.demo.tools.BookingTools;
import com.example.demo.tools.RevenueTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatbotConfig {

    private final ChatLanguageModel geminiChatModel;
    private final RoomTools roomTools;
    private final BookingTools bookingTools;
    private final RevenueTools revenueTools;

    @Bean
    public HotelAssistant hotelAssistant() {
        return AiServices.builder(HotelAssistant.class)
                .chatLanguageModel(geminiChatModel)
                .tools(roomTools, bookingTools, revenueTools)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}