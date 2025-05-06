package com.backend.orderservice.event;

import com.backend.orderservice.service.serviceImpl.OrderServiceImpl;
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
public class PaymentResultConsumer {

    private final OrderServiceImpl orderService;

    public PaymentResultConsumer(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    /**
     * Lắng nghe sự kiện kết quả thanh toán từ payment-service
     * 
     * @param paymentResult Kết quả thanh toán từ payment-service
     */
    @KafkaListener(topics = "payment-result-topic", groupId = "order-service-group")
    public void consumePaymentResult(Map<String, Object> paymentResult) {
        try {
            log.info("Nhận được kết quả thanh toán: {}", paymentResult);

            // Lấy thông tin từ sự kiện
            Long orderId = Long.valueOf(paymentResult.get("orderId").toString());
            boolean paymentSuccess = Boolean.parseBoolean(paymentResult.get("success").toString());

            // Xử lý kết quả thanh toán
//            orderService.handlePaymentResult(orderId, paymentSuccess);
            log.info("Đã xử lý kết quả thanh toán cho đơn hàng: {}", orderId);
        } catch (Exception e) {
            log.error("Lỗi khi xử lý kết quả thanh toán: {}", e.getMessage());
            // Có thể thêm logic để thử lại hoặc thông báo cho admin
        }
    }
}