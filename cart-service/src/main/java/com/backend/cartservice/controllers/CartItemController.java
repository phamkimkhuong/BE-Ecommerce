package com.backend.cartservice.controllers;

import com.backend.cartservice.dto.request.CreateCartItem;
import com.backend.cartservice.dto.request.UpdateCartItem;
import com.backend.commonservice.dto.reponse.CartItemReponse;
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
    public ResponseEntity<ApiResponseDTO<CartItemReponse>> updateCartItem(@RequestBody @Valid UpdateCartItem cartItem) {
        CartItemReponse updatedCartItem = cartItemService.updateCartItem(cartItem);
//        return updatedCartItem != null
//                ? ResponseEntity.ok(updatedCartItem) // Trả về chi tiết giỏ hàng đã cập nhật
//                : ResponseEntity.notFound().build(); // Trả về lỗi nếu không tìm thấy
        ApiResponseDTO<CartItemReponse> response = new ApiResponseDTO<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Cập nhật chi tiết giỏ hàng thành công");
        response.setData(updatedCartItem);
        return ResponseEntity.ok(response); // Trả về chi tiết giỏ hàng đã cập nhật
    }

    // API để xóa chi tiết giỏ hàng
    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponseDTO<String>> deleteCartItem(@PathVariable Long cartItemId) {
        boolean check = cartItemService.deleteCartItem(cartItemId);
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Xóa sản phẩm trong giỏ hàng thành công");
        response.setData(check + "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/plus/{cartItemId}")
    public ResponseEntity<String> plus(@PathVariable Long cartItemId){
        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        cartItemService.plus(cartItem);
        return new ResponseEntity<>("Plus ok", HttpStatus.OK);
    }

    @PostMapping("/minus/{cartItemId}")
    public ResponseEntity<String> minus(@PathVariable Long cartItemId){
        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        cartItemService.minus(cartItem);
        return new ResponseEntity<>("Minus ok", HttpStatus.OK);
    }

}
