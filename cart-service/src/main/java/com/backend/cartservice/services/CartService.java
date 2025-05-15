package com.backend.cartservice.services;

import com.backend.cartservice.entity.Cart;
import com.backend.commonservice.dto.reponse.CartResponse;

import java.util.Optional;
public interface CartService {
    // Thêm giỏ hàng
    Cart addCart(Long customerId);

    Optional<CartResponse> getCart(Long customerId);

    CartResponse getCartById(Long cartId);

    Cart updateCart(Long cartId, Cart cart);

    void deleteCart(Long cartId);
    Optional<CartResponse> getCartForKafkaConsumer(Long customerId);
}
