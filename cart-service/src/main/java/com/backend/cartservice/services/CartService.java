package com.backend.cartservice.services;

import com.backend.cartservice.entity.Cart;

import java.util.Optional;
public interface CartService {
    // Thêm giỏ hàng
    Cart addCart(Long customerId);

    Optional<Cart> getCart(Long customerId);

    Optional<Cart> getCartById(Long cartId);

    Cart updateCart(Long cartId, Cart cart);

    void deleteCart(Long cartId);
}
