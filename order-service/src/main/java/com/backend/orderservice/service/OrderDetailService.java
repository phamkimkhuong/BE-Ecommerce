package com.backend.orderservice.service;

import com.backend.orderservice.dtos.request.CreateOrderDetail;
import com.backend.orderservice.dtos.response.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailResponse> getAll();

    OrderDetailResponse getById(Long id);

    OrderDetailResponse save(CreateOrderDetail order);

    OrderDetailResponse update(Long id, CreateOrderDetail order);

    boolean delete(Long id);
}
