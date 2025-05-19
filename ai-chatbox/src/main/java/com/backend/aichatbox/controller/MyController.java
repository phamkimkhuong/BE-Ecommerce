package com.backend.aichatbox.controller;

import com.backend.aichatbox.service.ChatService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class MyController {
    ChatService chatClient;
    @PostMapping
    public ResponseEntity<String> getChatResponse(@RequestBody Map<String, String> message) {
        String question = message.get("question");
        String response = chatClient.getChatResponse(question);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/openai")
    public ResponseEntity<String> getChatResponseStream(@RequestBody String message) {
        String response = chatClient.getChatResponseOpenAi(message);
        return ResponseEntity.ok(response);
    }
}
