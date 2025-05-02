package com.backend.cartservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCartItem {
    private Long id; // ID chi tiết giỏ hàng
    @JsonProperty(value = "cart_id") // Liên kết với giỏ hàng
    private Long cartId; // Giỏ hàng mà sản phẩm này thuộc về
    @NotNull(message = "Mã sản phẩm không được để trống")
    @JsonProperty(value = "product_id") // Liên kết với sản phẩm
    private Long productId; // ID sản phẩm
    @NotNull(message = "Giá sản phẩm không được để trống")
    @Positive(message = "Giá sản phẩm phải lớn hơn 0")
    private double price; // Giá của sản phẩm
    @NotNull(message = "Số lượng sản phẩm không được để trống")
    @Positive(message = "Số lượng sản phẩm phải lớn hơn 0")
    private int quantity; // Số lượng sản phẩm trong giỏ hàng
    @Version
    private Long version;
}
