package com.backend.orderservice.event;

/*
 * @description: Producer để gửi sự kiện đơn hàng đến Kafka
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2023-05-15
 */

import com.backend.commonservice.event.EmailOrderEvent;
import com.backend.commonservice.event.OrderEvent;
import com.backend.commonservice.event.ProductEvent;
import com.backend.commonservice.service.KafkaService;
import com.backend.orderservice.domain.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderProducer {
    private static final String ORDER_TOPIC = "order-events";
    private static final String PRODUCT_TOPIC = "product-events";
    private static final String EMAIL_TOPIC = "email-events";

    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper;

    public OrderProducer(KafkaService kafkaService, ObjectMapper objectMapper) {
        this.kafkaService = kafkaService;
        this.objectMapper = objectMapper;
    }

    /**
     * Gửi sự kiện đơn hàng đến Kafka
     *
     * @param order Đơn hàng cần gửi sự kiện
     * @throws JsonProcessingException Nếu có lỗi khi chuyển đổi đơn hàng thành JSON
     * @throws Exception               Nếu có lỗi khi gửi sự kiện đến Kafka
     */
    public void sendOrderEvent(Order order) throws Exception {
        log.info("OrderProducer Nhận sự kiện đơn hàng {}", order);
        try {
            OrderEvent event = OrderEvent.fromOrder(order.getId(),
                    order.getCustomerId(),
                    order.getTrangThai(),
                    order.getTongTien(),
                    order.getThanhToanType(),
                    order.getEventType());
            String message = objectMapper.writeValueAsString(event);
            kafkaService.sendMessage(ORDER_TOPIC, message);
            log.info("Đã gửi sự kiện đơn hàng đến topic: {}", ORDER_TOPIC);
        } catch (JsonProcessingException e) {
            log.error("Lỗi khi chuyển đổi sự kiện đơn hàng thành JSON: {}", e.getMessage());
            throw e; // Truyền lỗi lên cho người gọi xử lý
        } catch (Exception e) {
            log.error("Lỗi khi gửi sự kiện đơn hàng: {}", e.getMessage());
            throw e; // Truyền lỗi lên cho người gọi xử lý
        }
    }
    /**
     * Gửi sự kiện email đến Kafka
     *
     * @param event Sự kiện email cần gửi
     */
    public void sendEmailEvent(Order event){
        log.info("OrderProducer Nhận sự kiện email");
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaService.sendMessage(EMAIL_TOPIC, message);
            log.info("Đã gửi sự kiện email đến topic: {}", EMAIL_TOPIC);
        } catch (JsonProcessingException e) {
            log.error("Lỗi khi chuyển đổi sự kiện email thành JSON: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi khi gửi sự kiện email: {}", e.getMessage());
        }
    }

    public void sendProductEvent(List<ProductEvent> event) throws Exception {
        log.info("ProductProducer Nhận sự kiện trừ tồn kho {}", event);
        try {
            String message = objectMapper.writeValueAsString(event);
            log.info("Sự kiện trừ tồn kho {}", message);
            kafkaService.sendMessage(PRODUCT_TOPIC, message);
            log.info("Đã gửi sự kiện trừ tồn kho đến topic: {}", PRODUCT_TOPIC);
        } catch (JsonProcessingException e) {
            log.error("Lỗi khi chuyển đổi sự kiện trừ tồn kho thành JSON: {}", e.getMessage());
            throw e; // Truyền lỗi lên cho người gọi xử lý
        } catch (Exception e) {
            log.error("Lỗi khi gửi sự kiện trừ tồn kho: {}", e.getMessage());
            throw e; // Truyền lỗi lên cho người gọi xử lý
        }
    }
}