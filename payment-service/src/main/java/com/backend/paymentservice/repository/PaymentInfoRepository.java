package com.backend.paymentservice.repository;

import com.backend.paymentservice.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
    PaymentInfo findByOrderId(Long orderId);

    PaymentInfo findByTransactionId(String transactionId);

    PaymentInfo findByVnpTxnRef(String vnpTxnRef);
}
