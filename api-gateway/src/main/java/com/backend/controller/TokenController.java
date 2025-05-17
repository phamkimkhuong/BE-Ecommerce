package com.backend.controller;

import com.backend.service.TokenCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class TokenController {

    private final TokenCacheService tokenCacheService;

    public TokenController(TokenCacheService tokenCacheService) {
        this.tokenCacheService = tokenCacheService;
    }

    @PostMapping("/storeToken")
    public ResponseEntity<Void> storeToken(@RequestParam long orderId, @RequestParam String token) {
        log.info("storeToken orderId: {}, token: {}", orderId, token);
        tokenCacheService.storeToken(orderId, token,10);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getToken")
    public ResponseEntity<String> getToken(@RequestParam long orderId) {
        Object token = tokenCacheService.getToken(orderId);
        return ResponseEntity.ok(token != null ? token.toString() : null);
    }
}
