package com.backend.cartservice.controllers;

import com.backend.cartservice.entity.Cart;
import com.backend.cartservice.services.CartService;
import com.backend.commonservice.dto.reponse.CartResponse;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // API để thêm giỏ hàng mới (sử dụng customerId từ CartRequest)
    @PostMapping()
    public ResponseEntity<Cart> addCart(@RequestBody @Valid CartRequest cartRequest
    ) {
        log.info("Thêm giỏ hàng mới cho customerId: {}", cartRequest.getCustomerId());
        Cart cart = cartService.addCart(cartRequest.getCustomerId());
        return ResponseEntity.ok(cart);
    }

    // API để lấy giỏ hàng của khách hàng theo customerId
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long customerId) {
        Optional<CartResponse> cart = cartService.getCart(customerId);
        return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{cartId}")
    public ApiResponseDTO<CartResponse> getCartById(@PathVariable Long cartId) {
        ApiResponseDTO<CartResponse> response = new ApiResponseDTO<>();
        CartResponse cart = cartService.getCartById(cartId);
        response.setData(cart);
        response.setMessage("Lấy giỏ hàng thành công");
        response.setCode(HttpStatus.OK.value());
        return response;
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
        // Lấy thông tin giỏ hàng để kiểm tra tồn tại
        Optional<CartResponse> cart = cartService.getCart(cartId);
        if (cart.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
