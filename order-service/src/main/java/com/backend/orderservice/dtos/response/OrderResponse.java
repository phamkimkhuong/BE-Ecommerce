package com.backend.orderservice.dtos.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    LocalDate ngayDatHang;
    Double tongTien;
    String status;
    Long customerId;
}
