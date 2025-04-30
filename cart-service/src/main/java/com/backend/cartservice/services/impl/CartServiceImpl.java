package com.backend.cartservice.services.impl;

import com.backend.cartservice.entity.Cart;
import com.backend.cartservice.entity.CartItem;
import com.backend.cartservice.repository.CartItemRepository;
import com.backend.cartservice.repository.CartRepository;
import com.backend.cartservice.repository.OpenFeignClient.CustomerClient;
import com.backend.cartservice.services.CartService;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerClient customerClient;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, CustomerClient customerClient) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerClient = customerClient;
    }

    // Thêm giỏ hàng
    public Cart addCart(Long customerId) {
        // Kiểm tra khách hàng có tồn tại không
        Cart cart = new Cart();
        try {
            ApiResponseDTO<Boolean> check = customerClient.checkUserExit(customerId);
            if (!check.getData()) {
                throw new RuntimeException("Khách hàng không tồn tại");
            }
            cart.setCustomerId(customerId);
        } catch (Exception e) {
            throw new AppException(ErrorMessage.USER_SERVER_ERROR);
        }
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
