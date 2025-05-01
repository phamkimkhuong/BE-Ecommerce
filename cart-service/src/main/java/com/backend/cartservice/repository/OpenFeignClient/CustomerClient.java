package com.backend.cartservice.repository.OpenFeignClient;

import com.backend.commonservice.configuration.FeignClientConfig;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = FeignClientConfig.class)
public interface CustomerClient {
    /**
     * Kiểm tra khách hàng có tồn tại hay không
     *
     * @param customerId ID của khách hàng cần kiểm tra
     * @return ApiResponseDTO chứa thông tin về tồn tại khach hàng
     */
    @GetMapping("/user/check-user/{id}")
    ApiResponseDTO<Boolean> checkUserExit(@PathVariable("id") Long customerId);
}
