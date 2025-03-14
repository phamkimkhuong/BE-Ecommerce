package fit.se.cartservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    private Long id; // ID giỏ hàng, sẽ được tạo tự động
    private Long customerId; // ID khách hàng, để liên kết với khách hàng

    @OneToMany(mappedBy = "cart") // Liên kết với các chi tiết giỏ hàng
    private List<CartItem> cartItems; // Danh sách sản phẩm trong giỏ hàng

    private double totalPrice; // Tổng giá trị của giỏ hàng

    public void updateTotalPrice() {
        this.totalPrice = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum(); // Cập nhật tổng giá trị giỏ hàng
    }
}
