package com.backend.commonservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductEvent {
    Long customerId;
    Long productId;
    int quantity;
    Long orderId;

    public static ProductEvent fromOrder(Long customerId, Long productId, int quantity, Long orderId) {
        return ProductEvent.builder()
                .customerId(customerId)
                .productId(productId)
                .quantity(quantity)
                .orderId(orderId)
                .build();
    }
}
