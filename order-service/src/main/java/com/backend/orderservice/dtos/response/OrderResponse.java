package com.backend.orderservice.dtos.response;

import com.backend.commonservice.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    LocalDateTime ngayDatHang;
    Double tongTien;
    String trangThai;
    Long customerId;
}
