package com.backend.aichatbox.service;

import java.util.Map;

public interface ChatService {
    String getChatResponse(String message);
    String getChatResponseOpenAi(String message);
}
