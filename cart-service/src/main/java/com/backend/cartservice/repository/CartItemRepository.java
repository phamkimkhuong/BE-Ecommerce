package com.backend.cartservice.repository;

import com.backend.cartservice.entity.CartItem;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
@RepositoryRestResource
@Hidden
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId); // Tìm các chi tiết giỏ hàng của giỏ hàng cụ thể
}
