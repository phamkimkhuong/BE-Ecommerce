package com.backend.cartservice.controllers;

import com.backend.cartservice.entity.CartItem;
import com.backend.cartservice.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    // API để thêm chi tiết giỏ hàng
    @PostMapping("/add")
    @PreAuthorize("@cartSecurityExpression.isCartOwnerOrAdmin(#cartItem.cart.id)")
    public ResponseEntity<CartItem> addCartItem(@RequestBody CartItem cartItem) {
        CartItem newCartItem = cartItemService.addCartItem(cartItem);
        return ResponseEntity.ok(newCartItem); // Trả về chi tiết giỏ hàng vừa được thêm
    }

    // API để lấy tất cả chi tiết giỏ hàng của một giỏ hàng
    @GetMapping("/{cartId}")
    @PreAuthorize("@cartSecurityExpression.isCartOwnerOrAdmin(#cartId)")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long cartId) {
        List<CartItem> cartItems = cartItemService.getCartItems(cartId);
        return ResponseEntity.ok(cartItems); // Trả về danh sách các chi tiết giỏ hàng
    }

    // API để cập nhật chi tiết giỏ hàng
    @PutMapping("/update")
    @PreAuthorize("@cartSecurityExpression.isCartOwnerOrAdmin(#cartItem.cart.id)")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem) {
        CartItem updatedCartItem = cartItemService.updateCartItem(cartItem);
        return updatedCartItem != null
                ? ResponseEntity.ok(updatedCartItem) // Trả về chi tiết giỏ hàng đã cập nhật
                : ResponseEntity.notFound().build(); // Trả về lỗi nếu không tìm thấy
    }

    // API để xóa chi tiết giỏ hàng
    @DeleteMapping("/delete/{cartItemId}")
    @PreAuthorize("@cartSecurityExpression.isCartItemOwnerOrAdmin(#cartItemId)")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
        // Lấy thông tin cartItem để kiểm tra tồn tại
        CartItem cartItem = (CartItem) cartItemService.getCartItems(cartItemId);
        if (cartItem == null) {
            return ResponseEntity.notFound().build();
        }

        cartItemService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build(); // Trả về thành công khi xóa
    }
}
