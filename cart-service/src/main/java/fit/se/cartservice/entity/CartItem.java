package fit.se.cartservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CartItem {

    @Id
    private Long id; // ID chi tiết giỏ hàng

    @ManyToOne
    private Cart cart; // Liên kết với giỏ hàng

    private Long productId; // ID sản phẩm
    private String productName; // Tên sản phẩm
    private double price; // Giá sản phẩm
    private int quantity; // Số lượng sản phẩm

    public double getTotalPrice() {
        return this.price * this.quantity; // Tính tổng giá trị sản phẩm trong giỏ hàng
    }
}
