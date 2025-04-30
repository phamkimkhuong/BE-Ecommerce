package com.backend.cartservice.repository;

import com.backend.cartservice.entity.Cart;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
@RepositoryRestResource
@Hidden
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomerId(Long customerId); // Trả về Optional để xử lý trường hợp không tìm thấy giỏ hàng
}
