package fit.se.cartservice.repository;


import fit.se.cartservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCustomerId(Long customerId); // Tìm giỏ hàng của một khách hàng
}
