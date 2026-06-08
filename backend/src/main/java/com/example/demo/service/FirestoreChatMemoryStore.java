package com.example.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@Profile("cloud")
@RequiredArgsConstructor
public class FirestoreChatMemoryStore implements ChatMemoryStore {

    private static final String COLLECTION = "chat_memories";

    private final Firestore firestore;
    private final ObjectMapper objectMapper;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        try {
            DocumentSnapshot snapshot = document(memoryId).get().get();
            if (!snapshot.exists()) {
                return new ArrayList<>();
            }

            String json = snapshot.getString("messages");
            if (json == null || json.isBlank()) {
                return new ArrayList<>();
            }

            List<Map<String, Object>> records = objectMapper.readValue(json, new TypeReference<>() {
            });
            return records.stream()
                    .map(this::deserialize)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("[ChatMemory] Failed to read Firestore memory for user {}: {}", memoryId, e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        try {
            List<Map<String, Object>> records = messages.stream()
                    .map(this::serialize)
                    .collect(Collectors.toList());

            Map<String, Object> data = Map.of(
                    "messages", objectMapper.writeValueAsString(records),
                    "updatedAt", Instant.now().toString());

            document(memoryId).set(data).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("[ChatMemory] Failed to update Firestore memory for user {}: {}", memoryId, e.getMessage());
        }
    }

    @Override
    public void deleteMessages(Object memoryId) {
        try {
            document(memoryId).delete().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("[ChatMemory] Failed to delete Firestore memory for user {}: {}", memoryId, e.getMessage());
        }
    }

    private DocumentReference document(Object memoryId) {
        return firestore.collection(COLLECTION).document(String.valueOf(memoryId));
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
            default -> log.warn("[ChatMemory] Unknown message type: {}", message.type());
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    private ChatMessage deserialize(Map<String, Object> map) {
        String type = (String) map.get("type");
        if (type == null) {
            return null;
        }

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
            default -> null;
        };
    }
}
