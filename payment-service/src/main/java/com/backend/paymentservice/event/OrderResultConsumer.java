package com.backend.paymentservice.event;

import com.backend.commonservice.enums.OrderStatus;
import com.backend.commonservice.enums.PaymentEventType;
import com.backend.commonservice.event.OrderEvent;
import com.backend.paymentservice.entity.PaymentInfo;
import com.backend.paymentservice.services.PaymentService;
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

/**
 * Consumer để lắng nghe sự kiện đơn hàng từ order-service
 *
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2024-05-15
 */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderResultConsumer {
    PaymentService paymentService;
    ObjectMapper objectMapper;
    PaymentProducer paymentProducer;

    @RetryableTopic(
            // 2 lần thử lại + 1 lần DLQ
            // Mặc định là 3 lần thử lại
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 10000),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            // Thử lại khi ngoại lệ là RuntimeException hoặc RetriableException
            include = {RuntimeException.class, RetriableException.class})
    /**
     * Lắng nghe sự kiện đơn hàng từ order-service
     *
     * @param orderResult Kết quả đơn hàng từ order-service
     */
    @KafkaListener(topics = "order-events")
    public void consumeOrderResult(String orderResult) {
        try {
            log.info("Nhận được sự kiện đơn hàng: {}", orderResult);
            OrderEvent orderEvent = objectMapper.readValue(orderResult, OrderEvent.class);
            switch (orderEvent.getEventType()) {
                case "CREATE":
                    // Xử lý đơn hàng mới - bắt đầu quá trình thanh toán
                    processNewOrder(orderEvent);
                    break;
                case "UPDATE":
                    // Cập nhật đơn hàng - có thể cần cập nhật thanh toán tương ứng
                    processOrderUpdate(orderEvent);
                    break;
                case "CANCEL":
                    // Hủy đơn hàng - có thể cần hoàn tiền
                    processOrderCancellation(orderEvent);
                    break;
                case "PAYMENT_FAILED":
                    // Ghi log cho việc thanh toán thất bại
                    log.info("Ghi nhận đơn hàng {} thanh toán thất bại", orderEvent.getId());
                    break;
                default:
                    log.warn("Không xử lý được loại sự kiện đơn hàng: {}", orderEvent.getEventType());
                    break;
            }
        } catch (Exception e) {
            log.error("Lỗi khi xử lý sự kiện đơn hàng: {}", e.getMessage(), e);
            // Gửi thông báo lỗi về order-service để xử lý (nếu cần)
        }
    }

    /**
     * Luu thong tin thanh toán cho đơn hàng mới
     *
     * @param orderEvent Sự kiện đơn hàng mới
     */
    private void processNewOrder(OrderEvent orderEvent) {
        log.info("Bắt đầu xử lý thanh toán cho đơn hàng: {}", orderEvent.getId());
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderId(orderEvent.getId());
        paymentInfo.setCustomerId(orderEvent.getCustomerId());
        paymentInfo.setAmount(orderEvent.getTongTien());
        paymentInfo.setPaymentMethod(orderEvent.getThanhToanType().name());
        paymentInfo.setPaymentEventType(PaymentEventType.PAYMENT_INITIATE);
        paymentInfo.setSuccess(false); // Chưa thanh toán thành công
        paymentService.save(paymentInfo);
    }

    /**
     * Xử lý cập nhật đơn hàng
     *
     * @param orderEvent Sự kiện cập nhật đơn hàng
     */
    private void processOrderUpdate(OrderEvent orderEvent) {
        // Xử lý cập nhật trạng thái đơn hàng
        // Ví dụ: khi đơn hàng bị hủy, cần hoàn tiền nếu đã thanh toán
        if (orderEvent.getTrangThai() == OrderStatus.HUY_DON) {
            processOrderCancellation(orderEvent);
        }
    }

    /**
     * Xử lý hủy đơn hàng
     *
     * @param orderEvent Sự kiện hủy đơn hàng
     */
    private void processOrderCancellation(OrderEvent orderEvent) {
        try {
            boolean refundSuccess = paymentService.processRefund(orderEvent.getId());
            log.info("Đã xử lý hoàn tiền cho đơn hàng bị hủy {}: {}",
                    orderEvent.getId(), refundSuccess ? "Thành công" : "Thất bại");
        } catch (Exception e) {
            log.error("Lỗi khi xử lý hoàn tiền cho đơn hàng bị hủy {}: {}", orderEvent.getId(), e.getMessage(), e);
        }
    }

    /**
     * Gửi thông báo lỗi thanh toán
     *
     * @param orderEvent   Thông tin đơn hàng
     * @param errorMessage Thông báo lỗi
     */
    private void sendPaymentFailureNotification(PaymentInfo orderEvent, String errorMessage) {
        try {
            paymentProducer.sendPaymentResult(orderEvent);
        } catch (Exception ex) {
            log.error("Không thể gửi thông báo lỗi thanh toán: {}", ex.getMessage());
        }
    }
}
