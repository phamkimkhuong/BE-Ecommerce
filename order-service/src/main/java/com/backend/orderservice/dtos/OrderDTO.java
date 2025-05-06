package com.backend.orderservice.dtos;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/22/2025 11:36 PM
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Tag(name = "Order Model", description = "Order Model Information")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    @Schema(description = "Mã đơn hàng", hidden = true)
    Long id;
    @NotNull
    @Schema(description = "Tổng tiền đơn hàng", example = "1000000")
    @JsonProperty(value = "tong_tien")
    Double tongTien;
    @Schema(description = "Trạng thái đơn hàng", example = "Đã giao hàng")
    @NotEmpty(message = "Trạng thái đơn hàng không được để trống")
    @JsonProperty(value = "trang_thai")
    String trangThai;
    @JsonProperty(value = "thanh_toan_type")
    String eventType;
    @Schema(description = "Mã khách hàng", example = "1")
    @NotNull(message = "Mã khách hàng không được để trống")
    @JsonProperty(value = "customer_id")
    Long customerId;
    @Schema(description = "Danh sách chi tiết đơn hàng")
    @JsonProperty(value = "order_details")
    List<OrderDetailDTO> orderDetails;
}
