package com.backend.cartservice.security;

import com.backend.cartservice.entity.CartItem;
import com.backend.cartservice.services.CartItemService;
import com.backend.cartservice.services.CartService;
import com.backend.commonservice.dto.reponse.CartResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class CartSecurityExpression {

    private final CartService cartService;

    private final CartItemService cartItemService;

    public CartSecurityExpression(CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    /**
     * Kiểm tra xem người dùng hiện tại có quyền truy cập vào giỏ hàng không
     *
     * @param cartId ID của giỏ hàng
     * @return true nếu người dùng là chủ sở hữu của giỏ hàng hoặc là admin
     */
    public boolean isCartOwner(Long cartId) {
        log.info("Kiểm tra quyền truy cập giỏ hàng với ID: {}", cartId);
        String userId = getUserIdFromRequest();
        Optional<CartResponse> cart = Optional.ofNullable(cartService.getCartById(cartId));
        return cart.isPresent() && Objects.equals(userId, cart.get().getCustomerId().toString());
    }

    /**
     * Kiểm tra xem người dùng hiện tại có quyền truy cập vào giỏ hàng của khách
     * hàng không
     *
     * @param customerId ID của khách hàng
     * @return true nếu người dùng là chủ sở hữu của giỏ hàng hoặc là admin
     */
    public boolean isCustomer(Long customerId) {
        String userId = getUserIdFromRequest();
        boolean check = userId != null && Objects.equals(customerId.toString(), userId);
        log.info("CustomerId: {}, UserID {}, Check {}", customerId, userId, check);
        return check;
    }

    /**
     * Lấy userId từ request hiện tại, ưu tiên từ header X-Auth-UserId
     *
     * @return userId của người dùng hiện tại
     */
    private String getUserIdFromRequest() {
        try {
            // Lấy request hiện tại
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                    .getRequest();
            // Ưu tiên lấy từ header X-Auth-UserId
            String headerUserId = request.getHeader("X-Auth-UserId");
            if (headerUserId != null && !headerUserId.isEmpty()) {
                return headerUserId;
            }
        } catch (Exception e) {
            log.warn("Không thể lấy request hiện tại: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Kiểm tra xem người dùng hiện tại có quyền truy cập vào mục giỏ hàng không
     *
     * @param cartItemId Mục giỏ hàng cần kiểm tra
     * @return true nếu người dùng là chủ sở hữu của giỏ hàng chứa mục đó hoặc là
     * admin
     */
    public boolean isCartItemOwner(Long cartItemId) {
        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        if (cartItem == null) {
            return false;
        }
        return isCartOwner(cartItem.getCart().getId());
    }

    /**
     * Kiểm tra xem người dùng hiện tại có quyền truy cập vào mục giỏ hàng không dựa
     * trên ID
     *
     * @param cartItemId ID của mục giỏ hàng cần kiểm tra
     * @return true nếu người dùng là chủ sở hữu của giỏ hàng chứa mục đó hoặc là
     * admin
     */
    public boolean isCartItemOwnerOrAdmin(Long cartItemId) {
        // Lấy thông tin cartItem từ service
        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        if (cartItem == null) {
            return false;
        }
        return isCartOwner(cartItem.getCart().getId());
    }
}