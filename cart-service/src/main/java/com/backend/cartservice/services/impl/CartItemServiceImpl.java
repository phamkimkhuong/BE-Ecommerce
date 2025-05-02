package com.backend.cartservice.services.impl;

import com.backend.cartservice.dto.request.CreateCartItem;
import com.backend.cartservice.dto.response.CartItemReponse;
import com.backend.cartservice.entity.CartItem;
import com.backend.cartservice.repository.CartItemRepository;
import com.backend.cartservice.repository.CartRepository;
import com.backend.cartservice.repository.OpenFeignClient.ProductClient;
import com.backend.cartservice.services.CartItemService;
import com.backend.commonservice.configuration.openFeign.FeignResponseException;
import com.backend.commonservice.dto.reponse.ProductReponse;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    private final ModelMapper modelMapper;

    private final CartRepository cartRepository; // Thêm CartRepository để tìm Cart

    private final ProductClient productClient; // Thêm ProductClient để kiểm tra số lượng sản phẩm

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CartItemServiceImpl.class);

    public CartItemServiceImpl(ModelMapper modelMapper, CartItemRepository cartItemRepository, CartRepository cartRepository, ProductClient productClient) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productClient = productClient;
        this.modelMapper = modelMapper;
    }

    // Convert Entity to DTO
    public CartItemReponse toCartItemRes(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemReponse.class);
    }

    // Convert DTO to Entity
    public CartItem toCartItem(CreateCartItem cartItem) {
        return modelMapper.map(cartItem, CartItem.class);
    }


    // Thêm chi tiết giỏ hàng
    @PreAuthorize("hasAuthority('ADMIN') or @cartSecurityExpression.isCartOwner(#cartItem.cartId)")
    public CartItemReponse addCartItem(CreateCartItem cartItem) {
        // Kiểm tra số lượng sản phẩm trong kho trước khi thêm vào giỏ hàng
        try {
            ApiResponseDTO<ProductReponse> response = productClient.checkProductAvailability(
                    cartItem.getProductId(), cartItem.getQuantity());
            log.info("Check product availability: {}", response);
        } catch (FeignResponseException e) {
            log.error("Lỗi khi kiểm tra số lượng sản phẩm: status={}, body={}", e.getStatus(), e.getResponseBody());
            // Xử lý dựa trên mã lỗi HTTP
            if (e.getStatus() == 404) {
                throw new AppException(ErrorMessage.PRODUCT_NOT_FOUND);
            } else if (e.getResponseBody().contains("\"code\":421")) {
                throw new AppException(ErrorMessage.PRODUCT_QUANTITY_NOT_ENOUGH);
            } else {
                throw new RuntimeException("Dịch vụ sản phẩm lỗi: " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("Lỗi không xác định: {}", e.getMessage());
            throw new RuntimeException("Lỗi hệ thống: " + e.getMessage());
        }
        CartItem cartItemEntity = toCartItem(cartItem); // Chuyển đổi từ DTO sang Entity
        // Lưu CartItem vào database
        CartItem c = cartItemRepository.save(cartItemEntity); // Lưu CartItem vào database
        // Chuyển đổi từ Entity sang DTO
        return toCartItemRes(c);
    }

    // Lấy tất cả chi tiết giỏ hàng của giỏ hàng cụ thể
    public List<CartItem> getCartItems(Long cartId) {
        return cartItemRepository.findByCartId(cartId); // Lấy tất cả CartItem theo cartId
    }

    // Cập nhật chi tiết giỏ hàng
    @Transactional
    public CartItem updateCartItem(CartItem cartItem) {
        // Kiểm tra nếu CartItem đã tồn tại
        CartItem existingCartItem = cartItemRepository.findById(cartItem.getId())
                .orElseThrow(() -> new AppException(ErrorMessage.CART_ITEM_NOT_FOUND));

        // Cập nhật giá trị của CartItem
        existingCartItem.setPrice(cartItem.getPrice());
        existingCartItem.setProductId(cartItem.getProductId());
        existingCartItem.setQuantity(cartItem.getQuantity());

        // Lưu CartItem đã cập nhật
        return cartItemRepository.save(existingCartItem); // Hoặc cartItemRepository.merge(existingCartItem);
    }

    // Xóa chi tiết giỏ hàng
    public void deleteCartItem(Long cartItemId) {
        if (cartItemRepository.existsById(cartItemId)) {
            cartItemRepository.deleteById(cartItemId); // Xóa chi tiết giỏ hàng nếu tồn tại
        } else {
            throw new AppException(ErrorMessage.CART_ITEM_NOT_FOUND); // Thông báo lỗi nếu không tìm thấy
        }
    }

    // Lấy chi tiết giỏ hàng theo ID
    public CartItem getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElse(null); // Trả về null nếu không tìm thấy
    }
}
