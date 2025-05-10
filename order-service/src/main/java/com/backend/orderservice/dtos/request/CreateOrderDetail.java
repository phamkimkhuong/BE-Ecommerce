package com.backend.orderservice.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDetail {
    private Long id;
    @NotNull(message = "Mã sản phẩm không được để trống")
    @JsonProperty("product_id")
    private Long productId;
    @NotNull(message = "Số lượng không được để trống")
    @JsonProperty("so_luong")
    private Integer soLuong;
    @NotNull(message = "Giá bán không được để trống")
    @JsonProperty("gia_ban")
    private Double giaBan;
    @NotNull(message = "Giá gốc không được để trống")
    @JsonProperty("gia_goc")
    private Double giaGoc;
    @NotNull(message = "Mã đơn hàng không được để trống")
    @JsonProperty("order_id")
    private Long orderId;
}
