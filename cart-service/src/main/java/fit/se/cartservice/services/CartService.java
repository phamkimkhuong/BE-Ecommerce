package fit.se.cartservice.services;

import fit.se.cartservice.entity.Cart;
import fit.se.cartservice.entity.CartItem;
import fit.se.cartservice.repository.CartItemRepository;
import fit.se.cartservice.repository.CartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    // Thêm giỏ hàng
    public Cart addCart(Long customerId) {
        Cart cart = new Cart();
        cart.setCustomerId(customerId);
        // Đảm bảo giỏ hàng được lưu vào DB
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
