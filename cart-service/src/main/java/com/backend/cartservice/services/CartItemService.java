package com.backend.cartservice.services;

import com.backend.cartservice.dto.request.CreateCartItem;
import com.backend.cartservice.dto.request.UpdateCartItem;
import com.backend.cartservice.dto.response.CartItemReponse;
import com.backend.cartservice.entity.CartItem;

import java.util.List;

public interface CartItemService {

    // Thêm chi tiết giỏ hàng
    CartItemReponse addCartItem(CreateCartItem cartItem);

    // Lấy tất cả chi tiết giỏ hàng của giỏ hàng cụ thể
    List<CartItem> getCartItems(Long cartId);

    CartItemReponse updateCartItem(UpdateCartItem cartItem);

    // Xóa chi tiết giỏ hàng
    boolean deleteCartItem(Long cartItemId);

    // Lấy chi tiết giỏ hàng theo ID
    CartItem getCartItemById(Long cartItemId);
}
