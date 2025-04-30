package com.backend.cartservice.services;

import com.backend.cartservice.entity.CartItem;

import java.util.List;

public interface CartItemService {

    // Thêm chi tiết giỏ hàng
    CartItem addCartItem(CartItem cartItem);

    // Lấy tất cả chi tiết giỏ hàng của giỏ hàng cụ thể
    List<CartItem> getCartItems(Long cartId);

    CartItem updateCartItem(CartItem cartItem);

    // Xóa chi tiết giỏ hàng
    void deleteCartItem(Long cartItemId);

    // Lấy chi tiết giỏ hàng theo ID
    CartItem getCartItemById(Long cartItemId);
}
