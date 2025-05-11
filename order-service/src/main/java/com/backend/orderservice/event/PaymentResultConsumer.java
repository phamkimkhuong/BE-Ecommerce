package com.backend.orderservice.event;

import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.backend.commonservice.event.PaymentEvent;
import com.backend.commonservice.event.ProductEvent;
import com.backend.orderservice.service.serviceImpl.OrderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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
public class PaymentResultConsumer {
    OrderServiceImpl orderService;
    ObjectMapper objectMapper;

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
            // Gửi sự kiện thất bại để các service khác có thể thực hiện rollback nếu cần
            try {
                // Gửi event thông báo lỗi để các service khác biết và xử lý
                // Map<String, Object> errorData = Map.of(
                // "orderId", paymentResult.get("orderId"),
                // "errorMessage", e.getMessage(),
                // "errorType", "PAYMENT_RESULT_PROCESSING_ERROR");
                // // Cần implement phương thức gửi sự kiện lỗi
                // orderService.notifyProcessingError(errorData);
                // Hoặc gửi một sự kiện khác nếu cần
                log.error("Gửi thông báo lỗi đến các service khác: {}", e.getMessage());
            } catch (Exception ex) {
                log.error("Không thể gửi thông báo lỗi: {}", ex.getMessage(), ex);
            }
        }
    }

    @KafkaListener(topics = "product-result-topic")
    public void consumeProductResult(String productResult) {
        try {
            log.info("Nhận được kết quả sản phẩm: {}", productResult);
            ApiResponseDTO p = objectMapper.readValue(productResult, ApiResponseDTO.class);
            int code = p.getCode();
            if (code == 200) {
                log.info("Cập nhật tồn kho thành công: {}", p.getMessage());
            } else {
                log.error("Cập nhật tồn kho thất bại: {}", p.getMessage());
                // Lấy orderId từ response nếu có
                Long orderId = null;
                try {
                    // Kiểm tra xem có dữ liệu tùy chỉnh trong response không
                    if (p.getData() != null && p.getData() instanceof Map) {
                        Map<String, Object> dataMap = (Map<String, Object>) p.getData();
                        if (dataMap.containsKey("orderId")) {
                            orderId = Long.valueOf(dataMap.get("orderId").toString());
                        }
                    }
                } catch (Exception ex) {
                    log.error("Lỗi khi lấy orderId từ response: {}", ex.getMessage());
                }

                // Gửi thông báo lỗi để xử lý
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("errorMessage", p.getMessage());
                errorData.put("errorType", "INVENTORY_UPDATE_ERROR");
                if (orderId != null) {
                    errorData.put("orderId", orderId);
                }
                orderService.notifyProcessingError(errorData);
            }
        } catch (Exception e) {
            log.error("Lỗi khi xử lý kết quả sản phẩm: {}", e.getMessage(), e);
        }
    }

}