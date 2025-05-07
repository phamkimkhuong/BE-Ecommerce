package com.backend.paymentservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentInfo {
    @Id
//    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    Long paymentId;
    Long orderId;
    Double amount;
    String transactionId;
    String paymentMethod;
    boolean success;
}
