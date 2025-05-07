package com.backend.orderservice.service.serviceImpl;

/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/21/2025 12:55 PM
 */

import com.backend.commonservice.dto.reponse.CartItemReponse;
import com.backend.commonservice.dto.reponse.CartResponse;
import com.backend.commonservice.enums.OrderStatus;
import com.backend.commonservice.enums.ThanhToanType;
import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import com.backend.orderservice.domain.Order;
import com.backend.orderservice.dtos.OrderDTO;
import com.backend.orderservice.dtos.request.CartOrderRequest;
import com.backend.orderservice.dtos.response.OrderDetailResponse;
import com.backend.orderservice.dtos.response.OrderResponse;
import com.backend.orderservice.event.OrderProducer;
import com.backend.orderservice.exception.PaymentException;
import com.backend.orderservice.repository.OpenFeignClient.CartClient;
import com.backend.orderservice.repository.OrderDetailRepository;
import com.backend.orderservice.repository.OrderRepository;
import com.backend.orderservice.service.OrderDetailService;
import com.backend.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.orderservice.domain.OrderDetail;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRep;
    ModelMapper modelMapper;
    OrderProducer orderProducer;
    OrderDetailService orderDetailService;
    CartClient cartClient;
    OrderDetailRepository orderDetailRepository;

    // Convert Entity to DTO
    public Order convertToEntity(OrderDTO product) {
        return modelMapper.map(product, Order.class);
    }

    // Convert DTO to Entity
    public OrderResponse convertToDTO(Order product) {
        return modelMapper.map(product, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getAll() {
        return orderRep.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public OrderResponse getById(Long id) {
        return orderRep.findById(id).map(this::convertToDTO).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
    }

    /**
     * Tính tổng tiền từ danh sách chi tiết giỏ hàng
     *
     * @param cartItemReponses Danh sách chi tiết giỏ hàng
     * @return Tổng tiền
     */
    private Double calculateTotalAmount(List<CartItemReponse> cartItemReponses) {
        if (cartItemReponses == null || cartItemReponses.isEmpty()) {
            return 0.0;
        }
        return cartItemReponses.stream()
                .mapToDouble(detail -> detail.getPrice() * detail.getPrice())
                .sum();
    }

    // chi co khach hang moi co quyen tao don hang
    @Transactional
    @Override
    // @PreAuthorize("hasAuthority('ADMIN')" or)
    public OrderResponse save(CartOrderRequest request) {
        log.info("Bắt đầu lưu đơn hàng : {}", request);
        Long createdOrderId = null;

        try {
            CartResponse cart = cartClient.getCartByID(request.getCartId());
            if (cart == null) {
                throw new AppException(ErrorMessage.RESOURCE_NOT_FOUND,
                        "Không tìm thấy giỏ hàng với ID: " + request.getCartId());
            }
            // Tính tổng tiền từ chi tiết đơn hàng
            Double totalAmount = calculateTotalAmount(cart.getCartItems());
            log.info("Tổng tiền đơn hàng: {}", totalAmount);

            // Tạo đơn hàng mới
            Order order = new Order();
            order.setTongTien(totalAmount);
            order.setTrangThai(OrderStatus.DANG_XU_LY);
            order.setEventType("CREATE");
            // chuyen doi thanh toan type
            order.setThanhToanType(ThanhToanType.fromVietnameseLabel(request.getHinhThucTT()));
            order.setNgayDatHang(LocalDate.now());
            Order result = orderRep.save(order);
            createdOrderId = result.getId(); // Lưu ID của đơn hàng đã tạo
            log.info("Đơn hàng đã được lưu thành công vào database:");
            // Lưu chi tiết đơn hàng
            List<CartItemReponse> dsCartItem = cart.getCartItems();
            for (CartItemReponse cartItem : dsCartItem) {
                OrderDetailResponse detailDTO = new OrderDetailResponse();
                detailDTO.setProductId(cartItem.getProductId());
                detailDTO.setSoLuong(cartItem.getQuantity());
                detailDTO.setGiaBan(cartItem.getPrice());
                detailDTO.setOrderId(result.getId());
                // Thiết lập giá gốc bằng giá bán (có thể cập nhật sau nếu có thông tin giá gốc
                // từ product-service)
                detailDTO.setGiaGoc(cartItem.getPrice());
                orderDetailService.save(detailDTO);
            }
            // Nếu không phải thanh toán khi nhận hàng, gửi sự kiện đến Kafka
            if (!order.getThanhToanType().equals(ThanhToanType.TT_KHI_NHAN_HANG)) {
                try {
                    // Gửi sự kiện đơn hàng mới đến Kafka
                    orderProducer.sendOrderEvent(result);
                    log.info("Đã gửi sự kiện đơn hàng đến payment-service: {}", result.getId());
                } catch (Exception e) {
                    log.error("Lỗi khi gửi sự kiện đơn hàng đến Kafka: {}", e.getMessage());
                    // Đánh dấu đơn hàng cần xử lý lại
                    result.setTrangThai(OrderStatus.LOI_THANH_TOAN);
                    orderRep.save(result);
                    throw new PaymentException("Lỗi khi xử lý thanh toán: " + e.getMessage());
                    // Có thể thêm logic để thử lại hoặc thông báo cho admin
                }
            }

            return convertToDTO(result);
        } catch (PaymentException e) {
            log.error("Lỗi thanh toán, tiến hành rollback đơn hàng: {}", e.getMessage());
            // Thực hiện rollback thủ công nếu đã tạo đơn hàng
            if (createdOrderId != null) {
                rollbackOrderCreation(createdOrderId);
            }
            throw new AppException(ErrorMessage.PAYMENT_FAILED);
        }
    }

    // Thêm phương thức rollback
    @Transactional
    protected void rollbackOrderCreation(Long orderId) {
        log.info("Bắt đầu rollback đơn hàng ID: {}", orderId);
        try {
            // Xóa chi tiết đơn hàng trước (ràng buộc khóa ngoại)
            orderDetailRepository.deleteById(orderId);
            log.info("Đã xóa chi tiết đơn hàng cho orderId: {}", orderId);
            // Xóa đơn hàng
            orderRep.deleteById(orderId);
            log.info("Đã xóa đơn hàng ID: {}", orderId);
        } catch (Exception ex) {
            log.error("Lỗi khi rollback đơn hàng: {}", ex.getMessage());
            // Không ném exception để tránh lỗi chồng lỗi
            // Có thể ghi log lỗi để admin xử lý thủ công sau
        }
    }

    @Transactional
    @Override
    public OrderResponse update(Long id, OrderDTO product) {
        Order existingOrder = orderRep.findById(id).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));

        // Lưu trạng thái cũ để kiểm tra xem có thay đổi không
        OrderStatus oldStatus = existingOrder.getTrangThai();

        Order orderToUpdate = convertToEntity(product);
        orderToUpdate.setId(id);
        String eventType = "UPDATE";
        orderToUpdate.setEventType(eventType);

        // Luôn lưu đơn hàng vào database trước
        Order updatedOrder = orderRep.save(orderToUpdate);
        log.info("Đơn hàng đã được cập nhật thành công trong database: {}", updatedOrder.getId());

        // Nếu trạng thái đơn hàng thay đổi, gửi sự kiện đến Kafka
        if (orderToUpdate.getTrangThai() != oldStatus) {
            try {
                orderProducer.sendOrderEvent(updatedOrder);
                log.info("Đã gửi sự kiện cập nhật đơn hàng đến Kafka: {}", updatedOrder.getId());
            } catch (Exception e) {
                log.error("Lỗi khi gửi sự kiện cập nhật đơn hàng đến Kafka: {}", e.getMessage());
                // Có thể thêm logic để thử lại hoặc thông báo cho admin
                // Không rollback transaction vì đơn hàng đã được cập nhật trong database
            }
        }

        return convertToDTO(updatedOrder);
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        orderRep.deleteById(id);
        return true;
    }

    /**
     * Xử lý kết quả thanh toán từ payment-service
     * Saga Pattern: Phương thức này hoàn tất giai đoạn thanh toán trong Saga flow
     *
     * @param orderId        ID của đơn hàng cần cập nhật
     * @param paymentSuccess true nếu thanh toán thành công, false nếu thất bại
     * @param transactionId  ID giao dịch từ payment-service (có thể null)
     * @return Đơn hàng đã được cập nhật
     */
    @Transactional
    public OrderResponse handlePaymentResult(Long orderId, boolean paymentSuccess, String transactionId) {
        log.info("Xử lý kết quả thanh toán cho đơn hàng: {}, kết quả: {}, transactionId: {}",
                orderId, paymentSuccess ? "Thành công" : "Thất bại", transactionId);

        Order order = orderRep.findById(orderId).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND, "Không tìm thấy đơn hàng với ID: " + orderId));

        if (paymentSuccess) {
            // Nếu thanh toán thành công, cập nhật trạng thái đơn hàng thành ĐÃ THANH TOÁN
            order.setTrangThai(OrderStatus.DA_THANH_TOAN);
            order.setEventType("PAYMENT_COMPLETED");
            log.info("Đơn hàng {} đã được thanh toán thành công", orderId);

            // Tiếp tục Saga flow - gửi sự kiện tới inventory-service để cập nhật tồn kho
            try {
                orderProducer.sendOrderEvent(order);
                log.info("Đã gửi sự kiện đơn hàng đã thanh toán đến inventory-service: {}", orderId);
            } catch (Exception e) {
                log.error("Lỗi khi gửi sự kiện đơn hàng đã thanh toán đến inventory-service: {}", e.getMessage());
                // Không rollback transaction vì thanh toán đã thành công
            }
        } else {
            // Nếu thanh toán thất bại, cập nhật trạng thái đơn hàng thành LỖI THANH TOÁN
            order.setTrangThai(OrderStatus.LOI_THANH_TOAN);
            order.setEventType("PAYMENT_FAILED");
            log.error("Thanh toán thất bại cho đơn hàng {}", orderId);

            // Trong Saga Pattern: Thực hiện bù trừ (compensating transaction)
            // Không cần thực hiện bù trừ với inventory service vì tại bước này inventory
            // chưa bị giảm
        }

        // Lưu đơn hàng với trạng thái mới
        Order updatedOrder = orderRep.save(order);

        return convertToDTO(updatedOrder);
    }

    /**
     * Gửi thông báo lỗi khi xử lý sự kiện trong Saga Pattern
     * Phương thức này gửi thông tin lỗi đến Kafka để các service khác biết và thực
     * hiện bù trừ nếu cần
     *
     * @param errorData Dữ liệu lỗi cần gửi
     */
    public void notifyProcessingError(Map<String, Object> errorData) {
        try {
            log.info("Gửi thông báo lỗi xử lý: {}", errorData);
            // Sử dụng OrderProducer để gửi sự kiện lỗi

            // Tạo sự kiện đơn hàng với trạng thái lỗi
            Long orderId = Long.valueOf(errorData.get("orderId").toString());
            Order order = orderRep.findById(orderId).orElse(new Order());
            order.setTrangThai(OrderStatus.LOI_XU_LY);
            order.setEventType("ERROR_PROCESSING");

            // Gửi sự kiện lỗi đến Kafka
            orderProducer.sendOrderEvent(order);
            log.info("Đã gửi thông báo lỗi cho orderId: {}", orderId);
        } catch (Exception e) {
            log.error("Không thể gửi thông báo lỗi: {}", e.getMessage(), e);
        }
    }
}
