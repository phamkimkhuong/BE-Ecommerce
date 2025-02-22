package com.backend.orderservice.repository;

import com.backend.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
public interface OrderRepository extends JpaRepository<Order, Long> {
}
