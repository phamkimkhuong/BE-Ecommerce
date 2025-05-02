package com.backend.cartservice.controllers;

import com.backend.cartservice.dto.request.CreateCartItem;
import com.backend.cartservice.dto.response.CartItemReponse;
import com.backend.cartservice.entity.CartItem;
import com.backend.cartservice.services.CartItemService;
import com.backend.commonservice.dto.request.ApiResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    // API để thêm chi tiết giỏ hàng
    @PostMapping("/add")
    public ResponseEntity<ApiResponseDTO<CartItemReponse>> addCartItem(@RequestBody @Valid CreateCartItem cartItem) {
        CartItemReponse newCartItem = cartItemService.addCartItem(cartItem);
        ApiResponseDTO<CartItemReponse> response = new ApiResponseDTO<>();
        response.setCode(HttpStatus.CREATED.value());
        response.setMessage("Thêm chi tiết giỏ hàng thành công");
        response.setData(newCartItem);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // API để lấy tất cả chi tiết giỏ hàng của một giỏ hàng
    @GetMapping("/{cartId}")
    @PreAuthorize("hasAuthority('ADMIN') or @cartSecurityExpression.isCartOwner(#cartId)")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long cartId) {
        List<CartItem> cartItems = cartItemService.getCartItems(cartId);

        return ResponseEntity.ok(cartItems); // Trả về danh sách các chi tiết giỏ hàng
    }

    // API để cập nhật chi tiết giỏ hàng
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN') or @cartSecurityExpression.isCartOwner(#cartItem.cart.id)")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem) {
        CartItem updatedCartItem = cartItemService.updateCartItem(cartItem);
        return updatedCartItem != null
                ? ResponseEntity.ok(updatedCartItem) // Trả về chi tiết giỏ hàng đã cập nhật
                : ResponseEntity.notFound().build(); // Trả về lỗi nếu không tìm thấy
    }

    // API để xóa chi tiết giỏ hàng
    @DeleteMapping("/delete/{cartItemId}")
    @PreAuthorize("hasAuthority('ADMIN') or @cartSecurityExpression.isCartItemOwnerOrAdmin(#cartItemId)")
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
