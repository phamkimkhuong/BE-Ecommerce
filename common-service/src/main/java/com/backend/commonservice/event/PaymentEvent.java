package com.backend.commonservice.event;

/*
 * @description: Model đại diện cho sự kiện thanh toán
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2024-05-15
 */

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
public class PaymentEvent {
    Long paymentId;
    Long orderId;
    Double amount;
    Boolean success;
    String transactionId;
    String paymentMethod;
    LocalDateTime timestamp;
    String eventType; // PAYMENT_INITIATED, PAYMENT_COMPLETED, PAYMENT_FAILED, PAYMENT_REVERSED
    String errorMessage;

    // Phương thức tiện ích để tạo sự kiện thanh toán thành công
    public static PaymentEvent paymentSuccess(Long paymentId, Long orderId, Double amount,
            String transactionId, String paymentMethod) {
        return PaymentEvent.builder()
                .paymentId(paymentId)
                .orderId(orderId)
                .amount(amount)
                .success(true)
                .transactionId(transactionId)
                .paymentMethod(paymentMethod)
                .timestamp(LocalDateTime.now())
                .eventType("PAYMENT_COMPLETED")
                .build();
    }

    // Phương thức tiện ích để tạo sự kiện thanh toán thất bại
    public static PaymentEvent paymentFailed(Long paymentId, Long orderId, Double amount,
            String paymentMethod, String errorMessage) {
        return PaymentEvent.builder()
                .paymentId(paymentId)
                .orderId(orderId)
                .amount(amount)
                .success(false)
                .paymentMethod(paymentMethod)
                .timestamp(LocalDateTime.now())
                .eventType("PAYMENT_FAILED")
                .errorMessage(errorMessage)
                .build();
    }
}