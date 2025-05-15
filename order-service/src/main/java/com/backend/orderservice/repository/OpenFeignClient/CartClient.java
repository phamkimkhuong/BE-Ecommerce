package com.backend.orderservice.repository.OpenFeignClient;

import com.backend.commonservice.configuration.FeignClientConfig;
import com.backend.commonservice.dto.reponse.CartResponse;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service", configuration = FeignClientConfig.class)
public interface CartClient {
    /**
     * Lấy thông tin giỏ hàng theo ID
     *
     * @param cartId ID của giỏ hàng
     * @return Giỏ hàng tương ứng với ID
     */
    @GetMapping("/api/carts/id/{cartId}")
    ApiResponseDTO<CartResponse> getCartById(@PathVariable Long cartId);

//    /**
//     * Xóa các sản phẩm đã chọn khỏi giỏ hàng
//     *
//     * @param cartId      ID của giỏ hàng
//     * @param cartItemIds Danh sách ID của các sản phẩm cần xóa
//     * @return true nếu xóa thành công, ngược lại là false
//     */
//    boolean removeCartItems(Long cartId, List<Long> cartItemIds);
}
