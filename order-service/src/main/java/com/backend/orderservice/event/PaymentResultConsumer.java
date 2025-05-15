package com.backend.orderservice.event;

import com.backend.commonservice.enums.OrderStatus;
import com.backend.commonservice.event.PaymentEvent;
import com.backend.orderservice.domain.Order;
import com.backend.orderservice.exception.PaymentException;
import com.backend.orderservice.repository.OrderRepository;
import com.backend.orderservice.service.serviceImpl.OrderServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
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
public class PaymentResultConsumer {
    OrderServiceImpl orderService;
    OrderProducer orderProducer;
    OrderRepository orderRep;
    ObjectMapper objectMapper;

    @RetryableTopic(
            // 2 lần thử lại + 1 lần DLQ
            // Mặc định là 3 lần thử lại
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 10000),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            // Thử lại khi ngoại lệ là RuntimeException hoặc RetriableException
            include = {RuntimeException.class, RetriableException.class})

    /**
     * Lắng nghe sự kiện kết quả thanh toán từ payment-service
     *
     * @param paymentResult Kết quả thanh toán từ payment-service
     */
    @KafkaListener(topics = "payment-result-topic")
    public void consumePaymentResult(String paymentResult) {
        try {
            log.info("Nhận được kết quả thanh toán: {}", paymentResult);
            // Chuyển đổi JSON thành đối tượng PaymentEvent
            PaymentEvent p = objectMapper.readValue(paymentResult, PaymentEvent.class);
            orderService.handlePaymentResult(p);
        } catch (Exception e) {
            log.error("Lỗi khi xử lý kết quả thanh toán: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "product-result-topic")
    public void consumeProductResult(String productResult) {
        try {
            log.info("Nhận được kết quả sản phẩm: {}", productResult);
            Map<String, Long> payload = objectMapper.readValue(productResult, new TypeReference<>() {
            });
            // Xử lý kết quả sản phẩm
            Order order = orderService.getByIdV(payload.get("orderId"));
            Map<String, Object> map = new HashMap<>();
            map.put("cartId", payload.get("cartId"));
            map.put("order", order);
            try {
                orderProducer.sendOrderEvent(map);
                log.info("Đã gửi sự kiện đơn hàng đến payment-service: {}", order.getId());
            } catch (Exception e) {
                log.error("Lỗi khi gửi sự kiện đơn hàng đến Kafka: {}", e.getMessage());
                // Đánh dấu đơn hàng cần xử lý lại
                order.setTrangThai(OrderStatus.LOI_THANH_TOAN);
                orderRep.save(order);
                throw new PaymentException("Lỗi khi xử lý thanh toán: " + e.getMessage());
                // Có thể thêm logic để thử lại hoặc thông báo cho admin
            }
        } catch (Exception e) {
            log.error("Lỗi khi xử lý kết quả sản phẩm: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "product-result-fail")
    public void consumeProductResultFail(String productResult) {
        try {
            log.info("Nhận được kết quả sản phẩm: {}", productResult);
            Long orderId = objectMapper.readValue(productResult, Long.class);
            // Xử lý kết quả sản phẩm
            Order order = orderService.getByIdV(orderId);
            try {
//                orderProducer.sendOrderEvent(order);
                log.info("Đã gửi sự kiện đơn hàng đến payment-service: {}", order.getId());
            } catch (Exception e) {
                log.error("Lỗi khi gửi sự kiện đơn hàng đến Kafka: {}", e.getMessage());
                // Đánh dấu đơn hàng cần xử lý lại
                order.setTrangThai(OrderStatus.LOI_THANH_TOAN);
                orderRep.save(order);
                throw new PaymentException("Lỗi khi xử lý thanh toán: " + e.getMessage());
                // Có thể thêm logic để thử lại hoặc thông báo cho admin
            }
        } catch (Exception e) {
            log.error("Lỗi khi xử lý kết quả sản phẩm: {}", e.getMessage(), e);
        }
    }


}