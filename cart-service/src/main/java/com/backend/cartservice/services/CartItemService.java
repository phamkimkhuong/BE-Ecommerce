package com.backend.cartservice.services;

import com.backend.cartservice.entity.Cart;
import com.backend.cartservice.entity.CartItem;
import com.backend.cartservice.repository.CartItemRepository;
import com.backend.cartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository; // Thêm CartRepository để tìm Cart

    // Thêm chi tiết giỏ hàng
    public CartItem addCartItem(CartItem cartItem) {
        // Tìm Cart mà CartItem thuộc về (giả sử cartId được cung cấp)
        Cart cart = cartRepository.findById(cartItem.getCart().getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Gán Cart vào CartItem
        cartItem.setCart(cart);

        // Lưu CartItem vào database
        return cartItemRepository.save(cartItem);
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
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        // Cập nhật giá trị của CartItem
        existingCartItem.setPrice(cartItem.getPrice());
        existingCartItem.setProductId(cartItem.getProductId());
        existingCartItem.setProductName(cartItem.getProductName());
        existingCartItem.setQuantity(cartItem.getQuantity());

        // Lưu CartItem đã cập nhật
        return cartItemRepository.save(existingCartItem); // Hoặc cartItemRepository.merge(existingCartItem);
    }

    // Xóa chi tiết giỏ hàng
    public void deleteCartItem(Long cartItemId) {
        if (cartItemRepository.existsById(cartItemId)) {
            cartItemRepository.deleteById(cartItemId); // Xóa chi tiết giỏ hàng nếu tồn tại
        } else {
            throw new RuntimeException("Cart item not found for id: " + cartItemId); // Thông báo lỗi nếu không tìm thấy
        }
    }
}
