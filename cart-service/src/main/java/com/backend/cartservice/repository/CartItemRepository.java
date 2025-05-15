package com.backend.cartservice.repository;

import com.backend.cartservice.entity.CartItem;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
@Hidden
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId); // Tìm các chi tiết giỏ hàng của giỏ hàng cụ thể

    void deleteCartItemByCart_Id(Long cartId);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, @NotNull(message = "Mã sản phẩm không được để trống") Long productId);
}
