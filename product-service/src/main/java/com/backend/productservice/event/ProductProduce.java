package com.backend.productservice.event;

import com.backend.commonservice.service.KafkaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ProductProduce {
    static String PRODUCT_TOPIC = "product-result-topic";
    KafkaService kafkaService;
    /**
     * Gửi sự kiện trừ tồn kho đến Kafka
     * @param event Sự kiện trừ tồn kho cần gửi
     * @throws Exception Nếu có lỗi khi gửi sự kiện
     */
    public void sendProductEvent(String event) throws Exception {
        log.info("ProductProducer Nhận sự kiện trừ tồn kho {}", event);
        try {
            kafkaService.sendMessage(PRODUCT_TOPIC, event);
            log.info("Đã gửi sự kiện trừ tồn kho đến topic: {}", PRODUCT_TOPIC);
        } catch (Exception e) {
            log.error("Lỗi khi gửi sự kiện trừ tồn kho: {}", e.getMessage(), e);
            throw e;
        }
    }
}
