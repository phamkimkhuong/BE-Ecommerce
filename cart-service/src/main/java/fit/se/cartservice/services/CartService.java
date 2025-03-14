package fit.se.cartservice.services;

import fit.se.cartservice.entity.Cart;
import fit.se.cartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // Thêm giỏ hàng
    public Cart addCart(Long customerId) {
        Cart cart = new Cart();
        cart.setCustomerId(customerId);
        return cartRepository.save(cart);
    }

    // Lấy giỏ hàng của khách hàng
    public Cart getCart(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    // Cập nhật giỏ hàng
    public Cart updateCart(Cart cart) {
        return cartRepository.save(cart);
    }

    // Xóa giỏ hàng
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
