package com.backend.orderservice.repository;

import com.backend.orderservice.domain.Order;
import com.backend.orderservice.domain.OrderDetail;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
@Hidden
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
