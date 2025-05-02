package com.backend.cartservice.repository.OpenFeignClient;

import com.backend.commonservice.configuration.FeignClientConfig;
import com.backend.commonservice.dto.reponse.ProductReponse;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service", configuration = FeignClientConfig.class)
public interface ProductClient {
    @PostMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
    Object createUser(@RequestBody String user);

    /**
     * Kiểm tra số lượng sản phẩm trong kho
     * 
     * @param productId ID của sản phẩm cần kiểm tra
     * @param quantity  Số lượng cần kiểm tra
     * @return ApiResponseDTO chứa thông tin về tình trạng sản phẩm
     */
    @GetMapping("/products/check-availability/{productId}")
    ApiResponseDTO<ProductReponse>  checkProductAvailability(
            @PathVariable("productId") Long productId,
            @RequestParam("quantity") int quantity);
}
