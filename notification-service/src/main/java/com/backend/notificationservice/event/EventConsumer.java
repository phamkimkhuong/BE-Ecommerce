package com.backend.notificationservice.event;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 3/8/2025 9:17 PM
 * @updated: 4/27/2025
 */

import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.backend.commonservice.event.OrderEvent;
import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import com.backend.commonservice.service.EmailService;
import com.backend.notificationservice.repository.AuthClient;
import com.backend.notificationservice.repository.UserClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventConsumer {
    ObjectMapper objectMapper;
    EmailService emailService;
    UserClient userClient;
    AuthClient authClient;

    @RetryableTopic(
            // 2 lần thử lại + 1 lần DLQ
            // Mặc định là 3 lần thử lại
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 10000),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            // Thử lại khi ngoại lệ là RuntimeException hoặc RetriableException
            include = {RuntimeException.class, RetriableException.class})

    @KafkaListener(topics = "order-events-suspects", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderEvent(String message) {
        try {
            log.info("Nhận được sự kiện đơn hàng -> {}", message);
            // Chuyển đổi JSON thành đối tượng OrderEvent
//            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);
            // Xử lý sự kiện đơn hàng dựa trên loại sự kiện
//            processOrderEvent(orderEvent);
        } catch (Exception e) {
            log.error("Lỗi khi xử lý sự kiện đơn hàng: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi xử lý sự kiện đơn hàng", e);
        }
    }


    /*
     * @description: Phương thức này lắng nghe sự kiện đơn hàng từ topic
     * "order-events" và xử lý chúng.
     * Nếu xảy ra lỗi trong quá trình xử lý, nó sẽ thử lại tối đa 3 lần với thời
     * gian chờ tăng dần.
     * Nếu tất cả các lần thử lại đều thất bại, tin nhắn sẽ được gửi đến Dead Letter
     * Topic (DLT).
     *
     * @param message: Tin nhắn JSON được tiêu thụ từ Kafka topic.
     */

    @KafkaListener(topics = "email-events", containerFactory = "kafkaListenerContainerFactory")
    public void consumeEmailEvent(String message) {
        try {
            log.info("Nhận được sự kiện email -> {}", message);
            // Chuyển đổi JSON thành đối tượng OrderEvent
            Map<String, Object> payload = objectMapper.readValue(message, new TypeReference<>() {
            });
            // chuyển đổi object payload thành OrderEvent
            OrderEvent orderEvent = objectMapper.convertValue(payload.get("order"), OrderEvent.class);
            log.info("OrderEvent: {}", orderEvent);
            Map<String, Object> user = objectMapper.convertValue(payload.get("user"), new TypeReference<>() {
            });
            log.info("User: {}", user);
            String email = (String) user.get("email");
            log.info("Email: {}", email);
            // Tạo Map chứa các placeholder cho template
            Map<String, Object> placeholder = createOrderEmailPlaceholders(orderEvent);
            // Gửi email sử dụng template và placeholder
            emailService.sendEmail(email, "Đặt hàng thành công", "email-template.ftl", placeholder, null);
            log.info("Đã gửi email thông báo đơn hàng thành công cho đơn hàng: {}", orderEvent.getId());
        } catch (Exception e) {
            log.error("Lỗi khi xử lý sự kiện email: {}", e.getMessage(), e);
            throw new AppException(ErrorMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    /*
     * @description: Xử lý sự kiện đơn hàng dựa trên loại sự kiện
     *
     * @param orderEvent: Sự kiện đơn hàng cần xử lý
     */
    private void processOrderEvent(OrderEvent orderEvent) {
        // Giả định địa chỉ email người nhận - trong thực tế cần lấy từ thông tin khách hàng
        String recipientEmail = "phamkhuong345436@gmail.com";
        String emailSubject = "";
        String templateName = "email-template.ftl";

        switch (orderEvent.getEventType()) {
            case "CREATE":
                log.info("Xử lý sự kiện tạo đơn hàng mới: {}", orderEvent.getId());
                emailSubject = "Đặt hàng thành công";
                break;
            case "UPDATE":
                log.info("Xử lý sự kiện cập nhật đơn hàng: {}, trạng thái: {}",
                        orderEvent.getId(), orderEvent.getTrangThai());
                emailSubject = "Cập nhật trạng thái đơn hàng";
                break;
            case "CANCEL":
                log.info("Xử lý sự kiện hủy đơn hàng: {}", orderEvent.getId());
                emailSubject = "Đơn hàng đã bị hủy";
                break;
            default:
                log.warn("Loại sự kiện không được hỗ trợ: {}", orderEvent.getEventType());
                return; // Không gửi email cho loại sự kiện không hỗ trợ
        }

        // Tạo Map chứa các placeholder cho template
        Map<String, Object> placeholder = createOrderEmailPlaceholders(orderEvent);

        // Gửi email thông báo
        try {
            emailService.sendEmail(recipientEmail, emailSubject, templateName, placeholder, null);
            log.info("Đã gửi email thông báo cho đơn hàng: {}, loại sự kiện: {}",
                    orderEvent.getId(), orderEvent.getEventType());
        } catch (Exception e) {
            log.error("Lỗi khi gửi email thông báo: {}", e.getMessage(), e);
        }
    }

    /**
     * Tạo Map chứa các placeholder cho template email đơn hàng
     *
     * @param orderEvent Thông tin sự kiện đơn hàng
     * @return Map chứa các placeholder cho template
     */
    private Map<String, Object> createOrderEmailPlaceholders(OrderEvent orderEvent) {
        Map<String, Object> placeholder = new HashMap<>();

        // Format tiền tệ theo định dạng Việt Nam
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String totalAmount = currencyFormatter.format(orderEvent.getTongTien());

        // Giả lập dữ liệu mẫu cho các sản phẩm trong đơn hàng
        String orderItems = "<tr>" +
                "<td>Sản phẩm mẫu</td>" +
                "<td>1</td>" +
                "<td>" + currencyFormatter.format(orderEvent.getTongTien()) + "</td>" +
                "<td>" + currencyFormatter.format(orderEvent.getTongTien()) + "</td>" +
                "</tr>";

        // Thêm các placeholder vào Map
        placeholder.put("customerName", orderEvent.getCustomerId() != null ? orderEvent.getCustomerId() : "Khách hàng");
        placeholder.put("orderId", orderEvent.getId());
        placeholder.put("orderDate", orderEvent.getNgayDatHang());
        placeholder.put("paymentMethod", "Thanh toán khi nhận hàng");
        placeholder.put("orderStatus", orderEvent.getTrangThai());
        placeholder.put("orderItems", orderItems);
        placeholder.put("subtotal", totalAmount);
        placeholder.put("shippingFee", currencyFormatter.format(0));
        placeholder.put("totalAmount", totalAmount);
        placeholder.put("recipientName", orderEvent.getCustomerId() != null ? orderEvent.getCustomerId() : "Khách hàng");
        placeholder.put("shippingAddress", "Địa chỉ mẫu, Quận 1, TP.HCM");
        placeholder.put("phoneNumber", "0123456789");
        placeholder.put("estimatedDelivery", "3-5 ngày làm việc");
        placeholder.put("trackingUrl", "http://example.com/tracking");
        placeholder.put("websiteUrl", "http://example.com");
        placeholder.put("privacyPolicyUrl", "http://example.com/privacy");
        placeholder.put("termsOfServiceUrl", "http://example.com/terms");

        return placeholder;
    }

    // Phương thức này xử lý các tin nhắn được gửi đến Dead Letter Topic (DLT).
    // Nó sẽ được gọi khi tin nhắn không thể xử lý sau tất cả các lần thử lại.
    // Tin nhắn sẽ được gửi đến DLT với cùng tên topic như topic gốc.
    @DltHandler
    public void handle(@Payload String message) {
        log.error("Xử lý tin nhắn từ DLT -> {}", message);
        // Lưu lỗi vào cơ sở dữ liệu hoặc gửi thông báo cho admin
    }
}
