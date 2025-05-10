package com.backend.orderservice.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO chứa thông tin yêu cầu tạo đơn hàng từ giỏ hàng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Yêu cầu tạo đơn hàng từ giỏ hàng")
public class CartOrderRequest {
    @NotNull(message = "Mã giỏ hàng không được để trống")
    @Schema(description = "Ma giỏ hàng của khách hàng", example = "1")
    @JsonProperty("cart_id")
    private Long cartId;

    @NotNull(message = "Mã Khách Hàng không được để trống")
    @Schema(description = "Ma giỏ hàng của khách hàng", example = "1")
    @JsonProperty("customer_id")
    private Long customerId;

    @NotBlank(message = "Hình thức thanh toán không được để trống")
    @JsonProperty("payment_method")
    private String hinhThucTT;

//    @NotNull(message = "Mã chi tiết giỏ hàng không được để trống")
//    @Schema(description = "Danh sách ID của các sản phẩm trong giỏ hàng cần đặt. Nếu không cung cấp, tất cả sản phẩm trong giỏ hàng sẽ được đặt.")
//    @JsonProperty("cart_item_ids")
//    private List<Long> cartItemIds;
}