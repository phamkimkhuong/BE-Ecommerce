package com.backend.paymentservice.dtos.request;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class CreatePayment {
    @Id
    private Long id;
    private Long orderId;
    private Double amount;
    private String paymentMethod;
    private boolean success;
    private String paymentUrl; // VNPay payment URL
    private String vnpTxnRef; // VNPay transaction reference
}
