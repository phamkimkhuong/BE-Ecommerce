package com.backend.commonservice.event;

/*
 * @description: Model đại diện cho sự kiện thanh toán
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2024-05-15
 */

import com.backend.commonservice.enums.PaymentEventType;
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
    Long customerId;
    Double amount;
    Boolean success;
    String paymentUrl; // VNPay payment URL
    String transactionId;
    String paymentMethod;
    PaymentEventType paymentEventType; // Loại sự kiện thanh toán
    String vnpTxnRef; // VNPay transaction reference
    LocalDateTime paymentDate;
    String bankCode;
    String cardType;
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
                .build();
    }
}