package com.backend.cartservice.services.impl;

import com.backend.cartservice.entity.Cart;
import com.backend.cartservice.entity.CartItem;
import com.backend.cartservice.repository.CartItemRepository;
import com.backend.cartservice.repository.CartRepository;
import com.backend.cartservice.services.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // Thêm giỏ hàng
    public Cart addCart(Long customerId) {
        // Kiểm tra xem khách hàng có tồn tại không
        if (cartRepository.findByCustomerId(customerId).isPresent()) {
            throw new RuntimeException("Giỏ hàng đã tồn tại cho khách hàng này");
        }
        Cart cart = new Cart();
        cart.setCustomerId(customerId);
        return cartRepository.save(cart);
    }

    // Lấy giỏ hàng của khách hàng
    public Optional<Cart> getCart(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    // Cập nhật giỏ hàng
    @Transactional
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


    // Xóa giỏ hàng
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
