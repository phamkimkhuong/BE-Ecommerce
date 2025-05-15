package com.backend.orderservice.repository.OpenFeignClient;

import com.backend.commonservice.configuration.FeignClientConfig;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "user-service", configuration = FeignClientConfig.class)
public interface UserClient {
    @GetMapping("/api/user/get-info")
    ResponseEntity<ApiResponseDTO<Map<String, Object>>> getUserInfo(@RequestParam(value = "id") Long id);
}
