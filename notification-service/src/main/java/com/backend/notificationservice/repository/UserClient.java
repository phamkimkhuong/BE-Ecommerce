package com.backend.notificationservice.repository;

import com.backend.commonservice.configuration.FeignClientConfig;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "user-service", configuration = FeignClientConfig.class)
public interface UserClient {
    @GetMapping("/user/{id}")
    ResponseEntity<ApiResponseDTO<Map<String, Object>>> getUserById(@PathVariable Long id);
}
