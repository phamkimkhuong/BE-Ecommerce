package com.backend.productservice.event;

import com.backend.commonservice.service.KafkaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ProductProduce {
    static String PRODUCT_TOPIC = "product-result-topic";
    static String PRODUCT_TOPIC_FAIL = "product-result-fail";
    KafkaService kafkaService;
    ObjectMapper objectMapper;

    /**
     * Gửi sự kiện trừ tồn kho đến Kafka
     *
     * @param payload Sự kiện trừ tồn kho cần gửi
     * @throws Exception Nếu có lỗi khi gửi sự kiện
     */
    public void sendProductEvent(Map<String, Long> payload) throws Exception {
        log.info("ProductProducer Nhận sự kiện trừ tồn kho {}", payload);

        String topic = (payload.get("orderId") != null) ? PRODUCT_TOPIC : PRODUCT_TOPIC_FAIL;
        if (payload.get("orderId") == null) {
            log.error("Sự kiện trừ tồn kho không hợp lệ");
        }
        try {
            String message = objectMapper.writeValueAsString(payload);
            kafkaService.sendMessage(topic, message);
            log.info("Đã gửi sự kiện trừ tồn kho đến topic: {}", topic);
        } catch (Exception e) {
            log.error("Lỗi khi gửi sự kiện trừ tồn kho: {}", e.getMessage(), e);
            throw e;
        }
    }
}
