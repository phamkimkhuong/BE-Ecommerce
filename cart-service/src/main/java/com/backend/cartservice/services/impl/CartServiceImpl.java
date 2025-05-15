package com.backend.cartservice.services.impl;

import com.backend.cartservice.entity.Cart;
import com.backend.cartservice.entity.CartItem;
import com.backend.cartservice.repository.CartItemRepository;
import com.backend.cartservice.repository.CartRepository;
import com.backend.cartservice.services.CartService;
import com.backend.commonservice.dto.reponse.CartResponse;
import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ModelMapper modelMapper;

    // Convert Entity to DTO
    public CartResponse toCartReponse(Cart cart) {
        return modelMapper.map(cart, CartResponse.class);
    }

    // Thêm giỏ hàng
    @PreAuthorize("hasAuthority('ADMIN') or @cartSecurityExpression.isCustomer(#customerId)")
    public Cart addCart(Long customerId) {
        // Kiểm tra xem khách hàng có tồn tại không
        if (cartRepository.findByCustomerId(customerId).isPresent()) {
            throw new AppException(ErrorMessage.CART_ALREADY_EXISTS);
        }
        Cart cart = new Cart();
        cart.setCustomerId(customerId);
        return cartRepository.save(cart);
    }

    // Lấy giỏ hàng của khách hàng
    @PreAuthorize("hasAuthority('ADMIN') or @cartSecurityExpression.isCustomer(#customerId)")
    public Optional<CartResponse> getCart(Long customerId) {
        Optional<Cart> c = Optional.ofNullable(cartRepository.findByCustomerId(customerId).orElseThrow(() ->
                new AppException(ErrorMessage.CART_NOT_FOUND)));
        return c.map(this::toCartReponse);
    }

    // Lấy giỏ hàng theo cartId
    public CartResponse getCartById(Long cartId) {
        log.info("CartServiceImpl Lấy giỏ hàng theo cartId: {}", cartId);
        Cart c = cartRepository.findById(cartId).orElseThrow(() ->
                new AppException(ErrorMessage.CART_NOT_FOUND));
        return toCartReponse(c);
    }

    // Cập nhật giỏ hàng
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or @cartSecurityExpression.isCartOwner(#cartId)")
    public Cart updateCart(Long cartId, Cart cart) {
        // Tìm giỏ hàng dựa trên cartId
        Optional<Cart> existingCartOpt = cartRepository.findById(cartId);

        // Kiểm tra nếu giỏ hàng tồn tại
        if (existingCartOpt.isPresent()) {
            Cart existingCart = existingCartOpt.get();

            // Cập nhật các thuộc tính của Cart (ví dụ: customerId)
            existingCart.setCustomerId(cart.getCustomerId());
            // Cập nhật các CartItem nếu cần
            for (CartItem updatedItem : cart.getCartItems()) {
                Optional<CartItem> existingItemOpt = cartItemRepository.findById(updatedItem.getId());
                if (existingItemOpt.isPresent()) {
                    CartItem existingItem = existingItemOpt.get();
                    // Cập nhật thông tin CartItem
                    existingItem.setQuantity(updatedItem.getQuantity());
                    existingItem.setPrice(updatedItem.getPrice());
                    cartItemRepository.save(existingItem);
                }
            }
            // Lưu lại giỏ hàng đã cập nhật
            cartRepository.save(existingCart);
            return existingCart;
        } else {
            return null; // Trả về null nếu không tìm thấy giỏ hàng
        }
    }

    @Transactional(readOnly = true)
    public Optional<CartResponse> getCartForKafkaConsumer(Long customerId) {
        Optional<Cart> c = cartRepository.findByCustomerId(customerId);
        return c.map(this::toCartReponse);
    }
    // Xóa giỏ hàng
    @PreAuthorize("hasAuthority('ADMIN') or @cartSecurityExpression.isCartOwner(#cartId)")
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
