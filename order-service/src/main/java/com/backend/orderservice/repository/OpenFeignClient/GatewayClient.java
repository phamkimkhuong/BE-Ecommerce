package com.backend.orderservice.repository.OpenFeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gateway-service", url = "${gateway.service.url}")
public interface GatewayClient {
    @PostMapping("/api/storeToken")
    ResponseEntity<Void> storeToken(@RequestParam long orderId, @RequestParam String token);

    @GetMapping("/api/getToken")
    ResponseEntity<String> getToken(@RequestParam long orderId);
}
