package com.backend.orderservice.repository.OpenFeignClient;

import com.backend.commonservice.configuration.FeignClientConfig;
import com.backend.commonservice.dto.reponse.CartItemReponse;
import com.backend.commonservice.dto.reponse.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "cart-service", configuration = FeignClientConfig.class)
public interface CartClient {

    /**
     * Lấy danh sách các sản phẩm đã chọn từ giỏ hàng
     *
     * @param cartId      ID của giỏ hàng
     * @param cartItemIds Danh sách ID của các sản phẩm được chọn
     * @return Danh sách thông tin chi tiết các sản phẩm
     */
    List<CartItemReponse> getSelectedCartItems(Long cartId, List<Long> cartItemIds);

    /**
     * Lấy thông tin giỏ hàng theo ID
     *
     * @param cartId ID của giỏ hàng
     * @return Giỏ hàng tương ứng với ID
     */
    @GetMapping("/api/carts/customer/{cartId}")
    CartResponse getCartByID(@PathVariable Long cartId);


    /**
     * Xóa các sản phẩm đã chọn khỏi giỏ hàng
     *
     * @param cartId      ID của giỏ hàng
     * @param cartItemIds Danh sách ID của các sản phẩm cần xóa
     * @return true nếu xóa thành công, ngược lại là false
     */
    boolean removeCartItems(Long cartId, List<Long> cartItemIds);
}
