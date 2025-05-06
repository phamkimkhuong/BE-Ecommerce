package com.backend.orderservice.service;

import com.backend.orderservice.dtos.OrderDTO;
import com.backend.orderservice.dtos.request.CartOrderRequest;
import com.backend.orderservice.dtos.response.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAll();

    OrderResponse getById(Long id);

    OrderResponse save(CartOrderRequest request);

    OrderResponse update(Long id, OrderDTO order);

    boolean delete(Long id);
}
