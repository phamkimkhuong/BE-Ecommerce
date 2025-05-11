package com.backend.productservice.event;

import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.backend.commonservice.event.ProductEvent;
import com.backend.productservice.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    /**
     * Lắng nghe sự kiện tôn kho từ order-service
     *
     * @param orderResult Kết quả thanh toán từ payment-service
     */
    @KafkaListener(topics = "product-events")
    public void consumeOrderResult(String orderResult) throws JsonProcessingException {
        ApiResponseDTO<Object> response = new ApiResponseDTO<>();
        try {
            log.info("Nhận được kết quả thanh toán: {}", orderResult);
            List<ProductEvent> ds = objectMapper.readValue(
                    orderResult,
                    new com.fasterxml.jackson.core.type.TypeReference<>() {
                    });
            productService.updateQuantityProduct(ds);
            response.setCode(200);
            response.setMessage("Cập nhật tồn kho thành công");
            response.setData(true);

        } catch (Exception e) {
            log.error("Lỗi khi xử lý kết quả tồn kho: {}", e.getMessage(), e);
            response.setCode(400);
            response.setMessage("Cập nhật tồn kho thất bại: " + e.getMessage());

            // Nếu có thông tin về orderId trong sự kiện, đưa vào response
            try {
                List<ProductEvent> events = objectMapper.readValue(
                        orderResult,
                        new com.fasterxml.jackson.core.type.TypeReference<>() {
                        });
                if (!events.isEmpty() && events.get(0).getOrderId() != null) {
                    // Tạo dữ liệu tùy chỉnh với orderId
                    Map<String, Object> customData = new HashMap<>();
                    customData.put("orderId", events.get(0).getOrderId());
                    customData.put("success", false);
                    response.setData(customData);
                } else {
                    response.setData(false);
                }
            } catch (Exception ex) {
                log.error("Không thể trích xuất orderId từ sự kiện: {}", ex.getMessage());
                response.setData(false);
            }
        }
        String message = objectMapper.writeValueAsString(response);
        try {
            productProduce.sendProductEvent(message);
        } catch (Exception e) {
            log.error("Lỗi khi gửi sự kiện tồn kho đến Kafka: {}", e.getMessage(), e);
        }
    }
}