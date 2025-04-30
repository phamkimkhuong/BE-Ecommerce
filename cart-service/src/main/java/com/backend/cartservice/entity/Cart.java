package com.backend.cartservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    @Transient
    private double totalPrice;

    public void updateTotalPrice() {
        this.totalPrice = cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        updateTotalPrice();
    }
}
