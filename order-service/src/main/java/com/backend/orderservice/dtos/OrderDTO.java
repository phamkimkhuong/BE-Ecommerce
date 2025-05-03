package com.backend.orderservice.dtos;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/22/2025 11:36 PM
 */

import com.backend.orderservice.domain.OrderDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Tag(name = "Order Model", description = "Order Model Information")
public class OrderDTO {
    @Schema(description = "Mã đơn hàng", hidden = true)
    private Long id;
//    @NotNull(message = "Ngày đặt hàng không được để trống")
    @Schema(description = "Ngày đặt hàng", example = "2022-02-22",hidden = true)
    private LocalDate ngayDatHang;
    @NotNull
    @Schema(description = "Tổng tiền đơn hàng", example = "1000000")
    private Double tongTien;
    @Schema(description = "Trạng thái đơn hàng", example = "Đã giao hàng")
    @NotEmpty(message = "Trạng thái đơn hàng không được để trống")
    private String status;
    @Schema(description = "Mã khách hàng", example = "1")
    @NotNull(message = "Mã khách hàng không được để trống")
    private Long customerId;
    @JsonIgnore
    private OrderDetail orderDetail;

}
