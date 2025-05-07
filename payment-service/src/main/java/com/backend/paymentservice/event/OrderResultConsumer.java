package com.backend.paymentservice.event;

import com.backend.commonservice.event.OrderEvent;
import com.backend.commonservice.enums.OrderStatus;
import com.backend.commonservice.enums.ThanhToanType;
import com.backend.paymentservice.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
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
public class OrderResultConsumer {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final PaymentProducer paymentProducer;

    /**
     * Lắng nghe sự kiện đơn hàng từ order-service
     * 
     * @param orderResult Kết quả đơn hàng từ order-service
     */
    @KafkaListener(topics = "order-events")
    public void consumeOrderResult(String orderResult) {
        try {
            log.info("Nhận được sự kiện đơn hàng: {}", orderResult);

            // Parse sự kiện đơn hàng
            OrderEvent orderEvent = objectMapper.readValue(orderResult, OrderEvent.class);

            // Xử lý sự kiện dựa trên loại
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
                    log.info("Ghi nhận đơn hàng {} thanh toán thất bại", orderEvent.getOrderId());
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
     * Xử lý đơn hàng mới - Bắt đầu quy trình thanh toán
     * 
     * @param orderEvent Sự kiện đơn hàng mới
     */
    private void processNewOrder(OrderEvent orderEvent) {
        // Chỉ xử lý các đơn hàng không phải thanh toán khi nhận hàng
        if (orderEvent.getHinhThucTT() != ThanhToanType.TT_KHI_NHAN_HANG) {
            try {
                log.info("Bắt đầu xử lý thanh toán cho đơn hàng: {}", orderEvent.getOrderId());

                // Gọi service để xử lý thanh toán
                boolean paymentSuccess = paymentService.processPayment(
                        orderEvent.getOrderId(),
                        orderEvent.getCustomerId(),
                        orderEvent.getTongTien(),
                        orderEvent.getHinhThucTT().name());

                // Gửi kết quả thanh toán về order-service
                if (paymentSuccess) {
                    // Lấy thông tin chi tiết thanh toán
                    Long paymentId = paymentService.getPaymentIdByOrderId(orderEvent.getOrderId());
                    String transactionId = paymentService.getTransactionIdByOrderId(orderEvent.getOrderId());

                    // Gửi thông báo thành công
                    paymentProducer.sendPaymentResult(
                            orderEvent.getOrderId(),
                            paymentId,
                            orderEvent.getTongTien(),
                            true,
                            transactionId,
                            orderEvent.getHinhThucTT().name(),
                            null);
                    log.info("Đã gửi kết quả thanh toán thành công cho đơn hàng: {}", orderEvent.getOrderId());
                } else {
                    // Gửi thông báo thất bại
                    paymentProducer.sendPaymentResult(
                            orderEvent.getOrderId(),
                            null,
                            orderEvent.getTongTien(),
                            false,
                            null,
                            orderEvent.getHinhThucTT().name(),
                            "Không thể xử lý thanh toán");
                    log.error("Thanh toán thất bại cho đơn hàng: {}", orderEvent.getOrderId());
                }
            } catch (Exception e) {
                log.error("Lỗi khi xử lý thanh toán cho đơn hàng {}: {}", orderEvent.getOrderId(), e.getMessage(), e);
                try {
                    // Gửi thông báo lỗi
                    paymentProducer.sendPaymentResult(
                            orderEvent.getOrderId(),
                            null,
                            orderEvent.getTongTien(),
                            false,
                            null,
                            orderEvent.getHinhThucTT().name(),
                            "Lỗi xử lý: " + e.getMessage());
                } catch (Exception ex) {
                    log.error("Không thể gửi thông báo lỗi thanh toán: {}", ex.getMessage());
                }
            }
        } else {
            log.info("Đơn hàng {} sử dụng thanh toán khi nhận hàng, bỏ qua xử lý thanh toán", orderEvent.getOrderId());
        }
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
            try {
                boolean refundSuccess = paymentService.processRefund(orderEvent.getOrderId());
                log.info("Đã xử lý hoàn tiền cho đơn hàng {}: {}",
                        orderEvent.getOrderId(), refundSuccess ? "Thành công" : "Thất bại");
            } catch (Exception e) {
                log.error("Lỗi khi xử lý hoàn tiền cho đơn hàng {}: {}", orderEvent.getOrderId(), e.getMessage(), e);
            }
        }
    }

    /**
     * Xử lý hủy đơn hàng
     * 
     * @param orderEvent Sự kiện hủy đơn hàng
     */
    private void processOrderCancellation(OrderEvent orderEvent) {
        try {
            boolean refundSuccess = paymentService.processRefund(orderEvent.getOrderId());
            log.info("Đã xử lý hoàn tiền cho đơn hàng bị hủy {}: {}",
                    orderEvent.getOrderId(), refundSuccess ? "Thành công" : "Thất bại");
        } catch (Exception e) {
            log.error("Lỗi khi xử lý hoàn tiền cho đơn hàng bị hủy {}: {}", orderEvent.getOrderId(), e.getMessage(), e);
        }
    }
}
