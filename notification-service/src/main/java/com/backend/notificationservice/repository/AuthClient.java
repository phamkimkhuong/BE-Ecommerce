package com.backend.notificationservice.repository;

import com.backend.commonservice.configuration.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", configuration = FeignClientConfig.class)
public interface AuthClient {
    @GetMapping("/account/get-email")
    ResponseEntity<?> getEmailUser(Long id);
}
