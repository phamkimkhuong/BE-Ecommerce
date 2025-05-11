package com.backend.orderservice.service.serviceImpl;

/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/21/2025 12:55 PM
 */

import com.backend.commonservice.dto.reponse.CartItemReponse;
import com.backend.commonservice.dto.reponse.CartResponse;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.backend.commonservice.enums.OrderStatus;
import com.backend.commonservice.enums.PaymentEventType;
import com.backend.commonservice.enums.ThanhToanType;
import com.backend.commonservice.event.PaymentEvent;
import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import com.backend.orderservice.domain.Order;
import com.backend.orderservice.domain.OrderDetail;
import com.backend.orderservice.dtos.OrderDTO;
import com.backend.orderservice.dtos.request.CartOrderRequest;
import com.backend.orderservice.dtos.request.CreateOrderDetail;
import com.backend.orderservice.dtos.response.OrderResponse;
import com.backend.orderservice.event.OrderProducer;
import com.backend.orderservice.exception.PaymentException;
import com.backend.orderservice.repository.OpenFeignClient.CartClient;
import com.backend.orderservice.repository.OrderDetailRepository;
import com.backend.orderservice.repository.OrderRepository;
import com.backend.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRep;
    ModelMapper modelMapper;
    OrderProducer orderProducer;
    CartClient cartClient;
    OrderDetailRepository orderDetailRepository;

    // Convert Entity to DTO
    public Order convertToEntity(OrderDTO product) {
        return modelMapper.map(product, Order.class);
    }

    //    Convert Entity to DTO
    public OrderDetail convertOrderDetailToEntity(CreateOrderDetail product) {
        return modelMapper.map(product, OrderDetail.class);
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
                .mapToDouble(detail -> detail.getPrice() * detail.getQuantity())
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
            ApiResponseDTO<CartResponse> response;
            try {
                response = cartClient.getCartById(request.getCartId());
            } catch (feign.RetryableException e) {
                log.error("Lỗi kết nối đến cart-service: {}", e.getMessage());
                throw new AppException(ErrorMessage.CART_SERVER_ERROR,
                        "Dịch vụ giỏ hàng không khả dụng, vui lòng thử lại sau");
            }
            if (response == null || response.getData() == null) {
                throw new AppException(ErrorMessage.CART_SERVER_ERROR);
            }
            CartResponse cart = response.getData();
            // Tính tổng tiền từ chi tiết đơn hàng
            Double totalAmount = calculateTotalAmount(cart.getCartItems());
            // Tạo đơn hàng mới
            Order order = new Order();
            order.setTongTien(totalAmount);
            order.setTrangThai(OrderStatus.DANG_XU_LY);
            order.setEventType("CREATE");
            // chuyen doi thanh toan type
            order.setThanhToanType(ThanhToanType.fromVietnameseLabel(request.getHinhThucTT()));
            order.setNgayDatHang(LocalDateTime.now());
            order.setCustomerId(request.getCustomerId());
            Order result = orderRep.save(order);
            createdOrderId = result.getId(); // Lưu ID của đơn hàng đã tạo
            // Lưu chi tiết đơn hàng
            List<CartItemReponse> dsCartItem = cart.getCartItems();
            if (dsCartItem.isEmpty()) {
                throw new AppException(ErrorMessage.CART_NOT_FOUND, "Đơn hàng không có sản phẩm nào");
            }
            for (CartItemReponse cartItem : dsCartItem) {
                OrderDetail detailDTO = new OrderDetail();
                detailDTO.setProductId(cartItem.getProductId());
                detailDTO.setSoLuong(cartItem.getQuantity());
                detailDTO.setGiaBan(cartItem.getPrice());
                detailDTO.setGiaGoc(cartItem.getPrice());
                order.setId(createdOrderId);
                detailDTO.setOrder(order);
                orderDetailRepository.save(detailDTO);
            }
            // Gửi sự kiện đến Kafka trừ số lượng hàng trong kho
            try {
                for(CartItemReponse cartItem : dsCartItem) {
                    orderProducer.sendProductEvent(cart.getCustomerId(),cartItem.getProductId(),cartItem.getQuantity());
                }
                log.info("Đã gửi sự kiện đơn hàng đến prodcut-service: {}", result.getId());
            } catch (Exception e) {
                log.error("Lỗi khi gửi sự kiện đơn hàng đến Kafka: {}", e.getMessage());
                result.setTrangThai(OrderStatus.LOI_XU_LY);
                orderRep.save(result);
                throw new PaymentException("Lỗi khi xử lý thanh toán: " + e.getMessage());
            }
            // Nếu không phải thanh toán khi nhận hàng, gửi sự kiện đến Kafka
            if (!order.getThanhToanType().equals(ThanhToanType.TT_KHI_NHAN_HANG)) {
                try {
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
     */
    @Transactional
    public void handlePaymentResult(PaymentEvent p) {
        log.info("Xử lý kết quả thanh toán cho đơn hàng");
        Order order = orderRep.findById(p.getOrderId()).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND, "Không tìm thấy đơn hàng cần thanh toán:"));
        if (p.getPaymentEventType().equals(PaymentEventType.PAYMENT_SUCCESS)) {
            order.setTrangThai(OrderStatus.DA_THANH_TOAN);
            orderRep.save(order);
            log.info("Đơn hàng đã được thanh toán thành công");
            // Tiếp tục Saga flow - gửi sự kiện tới inventory-service để cập nhật tồn kho
            try {
//                orderProducer.sendOrderEvent(order);
                log.info("Đã gửi sự kiện đơn hàng đã thanh toán đến inventory-service:");
            } catch (Exception e) {
                log.error("Lỗi khi gửi sự kiện đơn hàng đã thanh toán đến inventory-service: {}", e.getMessage());
            }
        } else {
            log.info("Lỗi thanh toán, cập nhật trạng thái đơn hàng thành LỖI THANH TOÁN");
            order.setTrangThai(OrderStatus.LOI_THANH_TOAN);
            orderRep.save(order);
            // Trong Saga Pattern: Thực hiện bù trừ (compensating transaction)
            // Không cần thực hiện bù trừ với inventory service vì tại bước này inventory
            // chưa bị giảm
        }
        // Lưu đơn hàng với trạng thái mới
        Order updatedOrder = orderRep.save(order);
        convertToDTO(updatedOrder);
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
