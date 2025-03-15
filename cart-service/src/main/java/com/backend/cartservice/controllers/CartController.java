package com.backend.cartservice.controllers;

import com.backend.cartservice.entity.Cart;
import com.backend.cartservice.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // API để thêm giỏ hàng mới (sử dụng customerId từ CartRequest)
    @PostMapping()
    public ResponseEntity<Cart> addCart(@RequestBody CartRequest cartRequest) {
        Cart cart = cartService.addCart(cartRequest.getCustomerId());
        return ResponseEntity.ok(cart);
    }

    // API để lấy giỏ hàng của khách hàng theo customerId
    @GetMapping("/{customerId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long customerId) {
        Optional<Cart> cart = cartService.getCart(customerId);
        return cart.isPresent() ? ResponseEntity.ok(cart.get()) : ResponseEntity.notFound().build();
    }

    // API để cập nhật giỏ hàng
    @PutMapping("/{cartId}")
    public ResponseEntity<Cart> updateCart(@PathVariable Long cartId, @RequestBody Cart cart) {
        // Cập nhật giỏ hàng theo cartId
        Cart updatedCart = cartService.updateCart(cartId, cart);
        if (updatedCart != null) {
            return ResponseEntity.ok(updatedCart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    // API để xóa giỏ hàng
    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
