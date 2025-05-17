package com.backend.orderservice.service.serviceImpl;

/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/21/2025 12:55 PM
 */

import com.backend.commonservice.configuration.openFeign.FeignResponseException;
import com.backend.commonservice.dto.reponse.CartItemReponse;
import com.backend.commonservice.dto.reponse.CartResponse;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.backend.commonservice.enums.OrderStatus;
import com.backend.commonservice.enums.PaymentEventType;
import com.backend.commonservice.enums.ThanhToanType;
import com.backend.commonservice.event.PaymentEvent;
import com.backend.commonservice.event.ProductEvent;
import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import com.backend.commonservice.model.TokenContext;
import com.backend.orderservice.domain.Order;
import com.backend.orderservice.domain.OrderDetail;
import com.backend.orderservice.dtos.OrderDTO;
import com.backend.orderservice.dtos.request.CartOrderRequest;
import com.backend.orderservice.dtos.response.OrderResponse;
import com.backend.orderservice.event.OrderProducer;
import com.backend.orderservice.exception.PaymentException;
import com.backend.orderservice.repository.OpenFeignClient.CartClient;
import com.backend.orderservice.repository.OpenFeignClient.GatewayClient;
import com.backend.orderservice.repository.OpenFeignClient.ProductClient;
import com.backend.orderservice.repository.OpenFeignClient.UserClient;
import com.backend.orderservice.repository.OrderDetailRepository;
import com.backend.orderservice.repository.OrderRepository;
import com.backend.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
    ProductClient productClient;
    UserClient userClient;
    GatewayClient gatewayClient;

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

    /**
     * Lấy danh sách đơn hàng theo ID khách hàng
     *
     * @param id ID khách hàng
     * @return Danh sách đơn hàng
     */
    @Override
    public OrderResponse getById(Long id) {
        return orderRep.findById(id).map(this::convertToDTO).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
    }

    /**
     * Lấy đơn hàng theo ID
     *
     * @param id ID đơn hàng
     * @return Đơn hàng
     */
    public Order getByIdV(Long id) {
        return orderRep.findById(id).orElseThrow(
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
            // Gọi dịch vụ giỏ hàng để lấy danh sách sản phẩm trong giỏ hàng
            try {
                response = cartClient.getCartById(request.getCartId());
            } catch (feign.RetryableException e) {
                log.error("Lỗi kết nối đến cart-service: {}", e.getMessage());
                throw new AppException(ErrorMessage.CART_SERVER_ERROR);
            }
            if (response == null || response.getData() == null) {
                throw new AppException(ErrorMessage.CART_SERVER_ERROR, "Không tìm thấy giỏ hàng");
            }
            // Kiểm tra xem giỏ hàng có sản phẩm nào không
            CartResponse cart = response.getData();
            List<CartItemReponse> dsCartItem = cart.getCartItems();
            if (dsCartItem.isEmpty()) {
                throw new AppException(ErrorMessage.CART_NOT_FOUND, "Đơn hàng không có sản phẩm nào");
            }
            // Kiểm tra số lượng sản phẩm trong kho trước khi tạo đơn hàng
            for (CartItemReponse cartItem : dsCartItem) {
                checkProductAvailability(cartItem.getProductId(), cartItem.getQuantity());
            }
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
            // Gửi sự kiện đến Kafka trừ tồn kho
            try {
                List<ProductEvent> productEvents = createProductEvents(cart, dsCartItem, createdOrderId);
                orderProducer.sendProductEvent(productEvents);
                log.info("Đã gửi sự kiện trừ tồn kho đến Kafka");
            } catch (Exception e) {
                log.error("Lỗi khi gửi sự kiện trừ tồn kho đến Kafka: {}", e.getMessage());
                result.setTrangThai(OrderStatus.LOI_XU_LY);
                orderRep.save(result);
                throw new AppException(ErrorMessage.INVENTORY_UPDATE_FAILED,
                        "Lỗi khi cập nhật tồn kho: " + e.getMessage());
            }
            // Chỉ tiếp tục xử lý thanh toán nếu cập nhật tồn kho thành công
            if (order.getThanhToanType().equals(ThanhToanType.TT_KHI_NHAN_HANG)) {
                Map<String, Object> map = new HashMap<>();
                map.put("order", result);
                Map<String, Object> user = getUser(result.getCustomerId());
                map.put("user", user);
                orderProducer.sendEmailEvent(map);
            }
            return convertToDTO(result);
        } catch (PaymentException e) {
            log.error("Lỗi thanh toán, tiến hành rollback đơn hàng: {}", e.getMessage());
            // Thực hiện rollback thủ công nếu đã tạo đơn hàng
            if (createdOrderId != null) {
                rollbackOrderCreation(createdOrderId);
            }
            throw new AppException(ErrorMessage.PAYMENT_FAILED);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    /**
     * Cập nhật đơn hàng theo ID
     *
     * @param id      ID của đơn hàng cần cập nhật
     * @param product Đối tượng OrderDTO chứa thông tin cập nhật
     * @return Đối tượng OrderResponse đã được cập nhật
     */
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
//                orderProducer.sendOrderEvent(updatedOrder);
                log.info("Đã gửi sự kiện cập nhật đơn hàng đến Kafka: {}", updatedOrder.getId());
            } catch (Exception e) {
                log.error("Lỗi khi gửi sự kiện cập nhật đơn hàng đến Kafka: {}", e.getMessage());
                // Có thể thêm logic để thử lại hoặc thông báo cho admin
                // Không rollback transaction vì đơn hàng đã được cập nhật trong database
            }
        }
        return convertToDTO(updatedOrder);
    }

    /**
     * Xóa đơn hàng theo ID
     *
     * @param id ID của đơn hàng cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy đơn hàng
     */
    @Transactional
    @Override
    public boolean delete(Long id) {
        orderRep.deleteById(id);
        return true;
    }

    @Override
    public Map<String, Object> getUser(Long id) {
        try {
            log.info("Lấy thông tin người dùng với ID: {}", id);
            ResponseEntity<ApiResponseDTO<Map<String, Object>>> response = userClient.getUserInfo(id);
            log.info("Kết quả từ user-service: {}", response);

            if (response != null && response.getBody() != null) {
                return response.getBody().getData();
            }
            return new HashMap<>();
        } catch (feign.RetryableException e) {
            log.error("Lỗi kết nối đến user-service: {}", e.getMessage(), e);
            throw new AppException(ErrorMessage.USER_SERVER_ERROR, "Không thể kết nối đến user-service");
        } catch (Exception e) {
            log.error("Lỗi không xác định khi gọi user-service: {}", e.getMessage(), e);
            throw new AppException(ErrorMessage.USER_SERVER_ERROR);
        }
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
            // gọi cart-service để xóa giỏ hàng
            try {
//                orderProducer.sendCartEvent(order.getCustomerId());
                Map<String, Object> map = new HashMap<>();
                map.put("order", order);
                ResponseEntity<String> getToken = gatewayClient.getToken(order.getId());
                String tokenFromGateway = getToken.getBody();
                TokenContext.setToken(tokenFromGateway);
                Map<String, Object> user = getUser(order.getCustomerId());
                map.put("user", user);
                orderProducer.sendEmailEvent(map);
                log.info("Đã gửi sự kiện đơn hàng đã thanh toán đến notifycation-service:");
                TokenContext.clear();
            } catch (Exception e) {
                log.error("Lỗi khi gửi sự kiện đơn hàng đã thanh toán đến inventory-service: {}", e.getMessage());
                throw new AppException(ErrorMessage.KAFKA_ERROR, e.getMessage());
            }
        } else {
            log.info("Lỗi thanh toán, cập nhật trạng thái đơn hàng thành LỖI THANH TOÁN");
            order.setTrangThai(OrderStatus.LOI_THANH_TOAN);
            orderRep.save(order);
            // gọi product-service để hoàn lại hàng
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

            // Xác định loại lỗi
            String errorType = (String) errorData.getOrDefault("errorType", "UNKNOWN_ERROR");
            String errorMessage = (String) errorData.getOrDefault("errorMessage", "Lỗi không xác định");
            Long orderId = null;

            if (errorData.containsKey("orderId")) {
                orderId = Long.valueOf(errorData.get("orderId").toString());
            }

            // Xử lý theo loại lỗi
            if ("INVENTORY_UPDATE_ERROR".equals(errorType)) {
                log.error("Lỗi cập nhật tồn kho: {}", errorMessage);
                // Nếu có orderId, cập nhật trạng thái đơn hàng
                if (orderId != null) {
                    Order order = orderRep.findById(orderId).orElse(null);
                    if (order != null) {
                        order.setTrangThai(OrderStatus.LOI_XU_LY);
                        order.setEventType("INVENTORY_ERROR");
                        orderRep.save(order);

                        // Gửi sự kiện lỗi đến Kafka
                        try {
//                            orderProducer.sendOrderEvent(order);
                            log.info("Đã gửi thông báo lỗi cập nhật tồn kho cho orderId: {}", orderId);
                        } catch (Exception e) {
                            log.error("Không thể gửi thông báo lỗi cập nhật tồn kho: {}", e.getMessage(), e);
                        }
                    }
                }
            } else {
                // Xử lý các loại lỗi khác
                if (orderId != null) {
                    Order order = orderRep.findById(orderId).orElse(new Order());
                    order.setTrangThai(OrderStatus.LOI_XU_LY);
                    order.setEventType("ERROR_PROCESSING");

                    // Gửi sự kiện lỗi đến Kafka
//                    orderProducer.sendOrderEvent(order);
                    log.info("Đã gửi thông báo lỗi cho orderId: {}", orderId);
                }
            }
        } catch (Exception e) {
            log.error("Không thể gửi thông báo lỗi: {}", e.getMessage(), e);
        }
    }

    /**
     * Kiểm tra số lượng sản phẩm trong kho
     *
     * @param productId ID sản phẩm
     * @param quantity  Số lượng cần kiểm tra
     */
    private void checkProductAvailability(Long productId, int quantity) {
        try {
            productClient.checkProductAvailability(productId, quantity);
        } catch (FeignResponseException e) {
            log.error("Lỗi khi kiểm tra số lượng sản phẩm: status={}, body={}", e.getStatus(), e.getResponseBody());
            // Xử lý dựa trên mã lỗi HTTP
            if (e.getResponseBody().contains("\"code\":421")) {
                throw new AppException(ErrorMessage.PRODUCT_QUANTITY_NOT_ENOUGH);
            }
        } catch (Exception e) {
            log.error("Lỗi không xác định: {}", e.getMessage());
            throw new AppException(ErrorMessage.PRODUCT_SERVER_ERROR);
        }
    }

    /**
     * Tạo danh sách sự kiện sản phẩm từ giỏ hàng
     *
     * @param cart      Giỏ hàng
     * @param cartItems Danh sách sản phẩm trong giỏ hàng
     * @param orderId   ID đơn hàng
     * @return Danh sách sự kiện sản phẩm
     */
    private List<ProductEvent> createProductEvents(CartResponse cart, List<CartItemReponse> cartItems, Long orderId) {
        List<ProductEvent> productEvents = new ArrayList<>();
        for (CartItemReponse cartItem : cartItems) {
            ProductEvent productEvent = new ProductEvent();
            productEvent.setCustomerId(cart.getCustomerId());
            productEvent.setProductId(cartItem.getProductId());
            productEvent.setQuantity(cartItem.getQuantity());
            productEvent.setOrderId(orderId);
            productEvent.setCartId(cart.getId());
            productEvents.add(productEvent);
        }
        return productEvents;
    }
}
