package com.backend.commonservice.event;

/*
 * @description: Model đại diện cho sự kiện đơn hàng
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2023-05-15
 */

import com.backend.commonservice.enums.OrderStatus;
import com.backend.commonservice.enums.ThanhToanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrderEvent {
    Long id;
    Long customerId;
    OrderStatus trangThai;
    Double tongTien;
    LocalDateTime ngayDatHang;
    ThanhToanType thanhToanType;
    String eventType; // Loại sự kiện: CREATE, UPDATE, CANCEL
    Long cartId; // ID giỏ hàng liên quan (nếu có)

    // Phương thức tiện ích để tạo sự kiện từ thông tin đơn hàng
    public static OrderEvent fromOrder(Long orderId, Long customerId,Long cartId, OrderStatus status, Double tongTien, ThanhToanType hinhThucTT, String eventType) {
        return OrderEvent.builder()
                .id(orderId)
                .customerId(customerId)
                .trangThai(status)
                .tongTien(tongTien)
                .ngayDatHang(LocalDateTime.now())
                .thanhToanType(hinhThucTT)
                .eventType(eventType) // Hoặc "UPDATE", "CANCEL" tùy thuộc vào loại sự kiện
                .cartId(cartId)
                .build();
    }
}