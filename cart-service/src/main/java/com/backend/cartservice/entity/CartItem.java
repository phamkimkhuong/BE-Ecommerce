package com.backend.cartservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tạo ID cho CartItem
    private Long id; // ID chi tiết giỏ hàng

    @ManyToOne
    @NotNull(message = "Mã giỏ hàng không được để trống")
    @JoinColumn(name = "cart_id", nullable = false) // Liên kết với giỏ hàng
    private Cart cart; // Giỏ hàng mà sản phẩm này thuộc về
    @NotNull(message = "Mã sản phẩm không được để trống")
    @Column(name = "product_id", nullable = false) // Liên kết với sản phẩm
    private Long productId; // ID sản phẩm
    @NotNull(message = "Giá sản phẩm không được để trống")
    @Positive(message = "Giá sản phẩm phải lớn hơn 0")
    private Double price; // Giá của sản phẩm
    @NotNull(message = "Số lượng sản phẩm không được để trống")
    @Positive(message = "Số lượng sản phẩm phải lớn hơn 0")
    private Integer quantity; // Số lượng sản phẩm trong giỏ hàng
    @Version
    private Long version;

    // Tính tổng giá trị của sản phẩm (số lượng * giá)
    public double getTotalPrice() {
        return this.price * this.quantity;
    }
}
