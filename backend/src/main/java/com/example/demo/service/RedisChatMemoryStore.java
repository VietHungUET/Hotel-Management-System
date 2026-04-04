package com.example.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Redis-backed ChatMemoryStore cho LangChain4j.
 *
 * Mỗi user có một key riêng biệt: "chat:memory:{userId}"
 * Memory tự động xóa sau 24 giờ không hoạt động (TTL).
 *
 * Các loại message được hỗ trợ:
 * - USER → UserMessage
 * - AI → AiMessage (có hoặc không có tool calls)
 * - SYSTEM → SystemMessage
 * - TOOL_RESULT → ToolExecutionResultMessage
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisChatMemoryStore implements ChatMemoryStore {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    private static final String KEY_PREFIX = "chat:memory:";
    private static final Duration TTL = Duration.ofHours(24);

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String key = KEY_PREFIX + memoryId;
        String json = stringRedisTemplate.opsForValue().get(key);

        if (json == null || json.isBlank()) {
            return new ArrayList<>();
        }

        try {
            List<Map<String, Object>> records = objectMapper.readValue(json, new TypeReference<>() {
            });
            return records.stream()
                    .map(this::deserialize)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[ChatMemory] Lỗi deserialize memory của user {}: {}", memoryId, e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String key = KEY_PREFIX + memoryId;

        try {
            List<Map<String, Object>> records = messages.stream()
                    .map(this::serialize)
                    .collect(Collectors.toList());

            String json = objectMapper.writeValueAsString(records);
            stringRedisTemplate.opsForValue().set(key, json, TTL);
        } catch (Exception e) {
            log.error("[ChatMemory] Lỗi serialize memory của user {}: {}", memoryId, e.getMessage());
        }
    }

    @Override
    public void deleteMessages(Object memoryId) {
        stringRedisTemplate.delete(KEY_PREFIX + memoryId);
        log.info("[ChatMemory] Đã xóa memory của user {}", memoryId);
    }

    private Map<String, Object> serialize(ChatMessage message) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", message.type().name());

        switch (message.type()) {
            case USER -> {
                UserMessage user = (UserMessage) message;
                map.put("text", user.singleText());
            }
            case AI -> {
                AiMessage ai = (AiMessage) message;
                if (ai.text() != null) {
                    map.put("text", ai.text());
                }
                if (ai.hasToolExecutionRequests()) {
                    List<Map<String, String>> toolRequests = ai.toolExecutionRequests().stream()
                            .map(r -> {
                                Map<String, String> req = new HashMap<>();
                                req.put("id", r.id());
                                req.put("name", r.name());
                                req.put("arguments", r.arguments());
                                return req;
                            })
                            .collect(Collectors.toList());
                    map.put("toolRequests", toolRequests);
                }
            }
            case SYSTEM -> {
                SystemMessage sys = (SystemMessage) message;
                map.put("text", sys.text());
            }
            case TOOL_EXECUTION_RESULT -> {
                ToolExecutionResultMessage tool = (ToolExecutionResultMessage) message;
                map.put("id", tool.id());
                map.put("toolName", tool.toolName());
                map.put("text", tool.text());
            }
            default -> log.warn("[ChatMemory] Loại message không nhận dạng được: {}", message.type());
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    private ChatMessage deserialize(Map<String, Object> map) {
        String type = (String) map.get("type");
        if (type == null)
            return null;

        return switch (type) {
            case "USER" -> UserMessage.from((String) map.get("text"));

            case "AI" -> {
                String text = (String) map.get("text");
                List<Map<String, String>> toolRequests = (List<Map<String, String>>) map.get("toolRequests");

                if (toolRequests != null && !toolRequests.isEmpty()) {
                    List<ToolExecutionRequest> requests = toolRequests.stream()
                            .map(r -> ToolExecutionRequest.builder()
                                    .id(r.get("id"))
                                    .name(r.get("name"))
                                    .arguments(r.get("arguments"))
                                    .build())
                            .collect(Collectors.toList());

                    // AiMessage có thể có cả text lẫn tool calls
                    if (text != null && !text.isBlank()) {
                        yield new AiMessage(text, requests);
                    }
                    yield AiMessage.from(requests);
                }

                yield AiMessage.from(text != null ? text : "");
            }

            case "SYSTEM" -> SystemMessage.from((String) map.get("text"));

            case "TOOL_EXECUTION_RESULT" -> ToolExecutionResultMessage.from(
                    (String) map.get("id"),
                    (String) map.get("toolName"),
                    (String) map.get("text"));

            default -> {
                log.warn("[ChatMemory] Bỏ qua message type không xác định: {}", type);
                yield null;
            }
        };
    }
}
