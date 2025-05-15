package com.backend.paymentservice.entity;

import com.backend.commonservice.enums.PaymentEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "transaction_id")
    private String transactionId; // ID giao dịch từ VNPay
    private boolean success;
    @Column(name = "payment_url", length = 2000)
    private String paymentUrl; // VNPay payment URL
    @Column(name = "vnp_txn_ref")
    private String vnpTxnRef; // VNPay transaction reference
    @Column(name = "payment_date")
    LocalDateTime paymentDate;
    @Column(name = "bank_code")
    String bankCode;
    @Column(name = "card_type")
    String cardType;
    @Column(name = "payment_event_type")
    PaymentEventType paymentEventType;
    @Column(name = "error_message")
    String errorMessage; // Lý do thanh toán thất bại
}
