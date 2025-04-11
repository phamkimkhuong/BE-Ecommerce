package com.backend.cartservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tạo ID cho CartItem
    private Long id; // ID chi tiết giỏ hàng

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false) // Liên kết với giỏ hàng
    private Cart cart; // Giỏ hàng mà sản phẩm này thuộc về

    @NotBlank(message = "Mã sản phẩm không được để trống")
    private Long productId; // ID sản phẩm
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName; // Tên sản phẩm
    @NotBlank(message = "Giá sản phẩm không được để trống")
    private double price; // Giá của sản phẩm
    @NotBlank(message = "Số lượng sản phẩm không được để trống")
    @Positive(message = "Số lượng sản phẩm phải lớn hơn 0")
    private int quantity; // Số lượng sản phẩm trong giỏ hàng
    @Version
    private Long version;
    // Tính tổng giá trị của sản phẩm (số lượng * giá)
    public double getTotalPrice() {
        return this.price * this.quantity;
    }
}
