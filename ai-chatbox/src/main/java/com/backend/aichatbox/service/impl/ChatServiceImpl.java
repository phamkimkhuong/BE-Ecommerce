package com.backend.aichatbox.service.impl;

import com.backend.aichatbox.service.ChatService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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

    WebClient webClient;
    public ChatServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public String getChatResponse(String message) {
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{Map.of("text", message)}),
                }
        );
        String response = webClient.post().uri(geminiApiUrl + geminiApiKey).header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
//        {
//            "candidates": [
//            {
//                "content": {
//                "parts": [
//                {
//                    "text":
//        Lấy  nội dung trong text
//                }
        return response;

    }
}
