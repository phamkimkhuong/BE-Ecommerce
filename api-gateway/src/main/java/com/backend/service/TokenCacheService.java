package com.backend.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    public TokenCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeToken(long orderId, String token, int minutes) {
        String key = "token_" + orderId;
        redisTemplate.opsForValue().set(key, token, minutes, TimeUnit.MINUTES);
    }

    public Object getToken(long orderId) {
        String key = "token_" + orderId;
        return redisTemplate.opsForValue().get(key);
    }

    public void removeToken(long orderId) {
        String key = "token_" + orderId;
        redisTemplate.delete(key);
    }
}