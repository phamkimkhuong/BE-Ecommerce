package com.backend.paymentservice.event;

import com.backend.commonservice.event.PaymentEvent;
import com.backend.commonservice.service.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Producer để gửi sự kiện thanh toán đến Kafka
 * 
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2024-05-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentProducer {
    private static final String PAYMENT_TOPIC = "payment-result-topic";

    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper;

    /**
     * Gửi sự kiện thanh toán thành công đến Kafka
     *
     * @param paymentEvent Sự kiện thanh toán cần gửi
     * @throws Exception Nếu có lỗi khi gửi sự kiện
     */
    public void sendPaymentEvent(PaymentEvent paymentEvent) throws Exception {
        log.info("Gửi sự kiện thanh toán: {}", paymentEvent);
        try {
            String message = objectMapper.writeValueAsString(paymentEvent);
            kafkaService.sendMessage(PAYMENT_TOPIC, message);
            log.info("Đã gửi sự kiện thanh toán thành công đến topic: {}", PAYMENT_TOPIC);
        } catch (JsonProcessingException e) {
            log.error("Lỗi khi chuyển đổi sự kiện thanh toán thành JSON: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Lỗi khi gửi sự kiện thanh toán: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Gửi kết quả thanh toán dưới dạng Map (phương thức tiện ích cho các trường hợp
     * đơn giản)
     *
     * @param orderId       ID đơn hàng
     * @param paymentId     ID thanh toán
     * @param amount        Số tiền thanh toán
     * @param success       Kết quả thanh toán (true: thành công, false: thất bại)
     * @param transactionId ID giao dịch (có thể null nếu thanh toán thất bại)
     * @param paymentMethod Phương thức thanh toán
     * @param errorMessage  Thông báo lỗi (có thể null nếu thanh toán thành công)
     * @throws Exception Nếu có lỗi khi gửi sự kiện
     */
    public void sendPaymentResult(Long orderId, Long paymentId, Double amount, boolean success,
            String transactionId, String paymentMethod, String errorMessage) throws Exception {
        PaymentEvent event;
        if (success) {
            event = PaymentEvent.paymentSuccess(paymentId, orderId, amount, transactionId, paymentMethod);
        } else {
            event = PaymentEvent.paymentFailed(paymentId, orderId, amount, paymentMethod, errorMessage);
        }
        sendPaymentEvent(event);
    }
}