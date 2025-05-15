package com.backend.userservice.repositories;

import com.backend.commonservice.configuration.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "auth-service", configuration = FeignClientConfig.class)
public interface AuthClient {
    @GetMapping("/api/account/get-email")
    ResponseEntity<Map<String, Object>> getEmailUser(@RequestParam(value = "id") Long id);
}
