package com.backend.orderservice.dtos;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/23/2025 9:45 AM
 */

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Tag(name = "OrderDetail Model", description = "OrderDetail Model Information")
public class OrderDetailDTO {
    @Schema(description = "Mã chi tiết đơn hàng", hidden = true)
    private Long id;
    @Schema(description = "Mã đơn hàng", example = "1")
    @NotNull(message = "Mã đơn hàng không được để trống")
    private Long orderId;
    @Schema(description = "Mã sản phẩm", example = "1")
    @NotNull(message = "Mã sản phẩm không được để trống")
    private Long productId;
    @Schema(description = "Số lượng sản phẩm", example = "1")
    @NotNull(message = "Số lượng sản phẩm không được để trống")
    private Integer soLuong;
    @Schema(description = "Giá bán sản phẩm", example = "30000000")
    @NotNull(message = "Giá bán sản phẩm không được để trống")
    private Double giaBan;
    @Schema(description = "Giá gốc sản phẩm", example = "25000000")
    @NotNull(message = "Giá gốc sản phẩm không được để trống")
    private Double giaGoc;
}
