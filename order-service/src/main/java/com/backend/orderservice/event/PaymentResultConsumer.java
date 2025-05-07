package com.backend.orderservice.event;

import com.backend.commonservice.enums.OrderStatus;
import com.backend.orderservice.service.serviceImpl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Consumer để lắng nghe kết quả thanh toán từ payment-service
 * 
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/25/2025 10:30 AM
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResultConsumer {

    private final OrderServiceImpl orderService;

    /**
     * Lắng nghe sự kiện kết quả thanh toán từ payment-service
     * 
     * @param paymentResult Kết quả thanh toán từ payment-service
     */
    @KafkaListener(topics = "payment-result-topic")
    public void consumePaymentResult(Map<String, Object> paymentResult) {
        try {
            log.info("Nhận được kết quả thanh toán: {}", paymentResult);

            // Lấy thông tin từ sự kiện
            Long orderId = Long.valueOf(paymentResult.get("orderId").toString());
            boolean paymentSuccess = Boolean.parseBoolean(paymentResult.get("success").toString());
            String transactionId = paymentResult.get("transactionId") != null
                    ? paymentResult.get("transactionId").toString()
                    : null;

            // Xử lý kết quả thanh toán - Saga continuation
            orderService.handlePaymentResult(orderId, paymentSuccess, transactionId);

            log.info("Đã xử lý kết quả thanh toán cho đơn hàng: {}", orderId);
        } catch (Exception e) {
            log.error("Lỗi khi xử lý kết quả thanh toán: {}", e.getMessage(), e);
            // Gửi sự kiện thất bại để các service khác có thể thực hiện rollback nếu cần
            try {
                // Gửi event thông báo lỗi để các service khác biết và xử lý
                Map<String, Object> errorData = Map.of(
                        "orderId", paymentResult.get("orderId"),
                        "errorMessage", e.getMessage(),
                        "errorType", "PAYMENT_RESULT_PROCESSING_ERROR");
                // Cần implement phương thức gửi sự kiện lỗi
                orderService.notifyProcessingError(errorData);
            } catch (Exception ex) {
                log.error("Không thể gửi thông báo lỗi: {}", ex.getMessage(), ex);
            }
        }
    }
}