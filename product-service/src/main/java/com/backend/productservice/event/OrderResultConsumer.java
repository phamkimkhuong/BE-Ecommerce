package com.backend.productservice.event;

import com.backend.commonservice.event.ProductEvent;
import com.backend.productservice.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
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
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderResultConsumer {
    ProductService productService;
    ObjectMapper objectMapper;
    ProductProduce productProduce;

    @RetryableTopic(
            // 2 lần thử lại + 1 lần DLQ
            // Mặc định là 3 lần thử lại
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 10000),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            // Thử lại khi ngoại lệ là RuntimeException hoặc RetriableException
            include = {RuntimeException.class, RetriableException.class})

    /**
     * Lắng nghe sự kiện tôn kho từ order-service
     *
     * @param orderResult Kết quả thanh toán từ payment-service
     */
    @KafkaListener(topics = "product-events")
    public void consumeOrderResult(String orderResult) throws JsonProcessingException {
        Long orderId = null;
        Long cartId = null;
        try {
            log.info("Nhận được thông tin tồn kho từ order: {}", orderResult);
            List<ProductEvent> ds = objectMapper.readValue(
                    orderResult,
                    new com.fasterxml.jackson.core.type.TypeReference<>() {
                    });
            productService.updateQuantityProduct(ds);
            orderId = ds.getFirst().getOrderId();
            cartId = ds.getFirst().getCartId();
        } catch (Exception e) {
            log.error("Lỗi khi xử lý kết quả tồn kho: {}", e.getMessage(), e);
        }
        // gủi cartid và orderId về cho order-service
        Map<String, Long> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("cartId", cartId);
        try {
            productProduce.sendProductEvent(payload);
        } catch (Exception e) {
            log.error("Lỗi khi gửi sự kiện tồn kho đến Kafka: {}", e.getMessage(), e);
        }
    }
}