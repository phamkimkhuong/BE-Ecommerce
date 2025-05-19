package com.backend.aichatbox.service.impl;

import com.backend.aichatbox.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatServiceImpl implements ChatService {
    @Value("${gemini.api.key}")
    String geminiApiKey;
    @Value("${gemini.api.url}")
    String geminiApiUrl;

    ChatClient chatClient;
    final WebClient webClient;

    public ChatServiceImpl(WebClient.Builder webClientBuilder,
                           ChatClient.Builder chatClient) {
        this.webClient = webClientBuilder.build();
//        this.chatClient = chatClient.defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())).build();
        this.chatClient = chatClient.build();
    }

    @Override
    public String getChatResponse(String message) {
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{Map.of("text", message)}),
                }
        );
        String jsonResponse = webClient.post().uri(geminiApiUrl + geminiApiKey).header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        // Sử dụng Jackson hoặc Gson để phân tích JSON và trích xuất text
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            // Trích xuất chỉ phần text từ phản hồi
            return rootNode
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu có
            return "Error extracting response: " + e.getMessage();
        }
    }
    @Override
    public String getChatResponseOpenAi(String message) {
        return chatClient.prompt(message)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
    }
}
