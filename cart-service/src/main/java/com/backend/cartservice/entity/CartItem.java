package com.backend.cartservice.entity;

import jakarta.persistence.*;
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

    private Long productId; // ID sản phẩm
    private String productName; // Tên sản phẩm
    private double price; // Giá của sản phẩm
    private int quantity; // Số lượng sản phẩm trong giỏ hàng
    @Version
    private Long version;
    // Tính tổng giá trị của sản phẩm (số lượng * giá)
    public double getTotalPrice() {
        return this.price * this.quantity;
    }
}
