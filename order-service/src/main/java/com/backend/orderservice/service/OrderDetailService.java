package com.backend.orderservice.service;

import com.backend.orderservice.dtos.OrderDetailDTO;
import com.backend.orderservice.dtos.response.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailResponse> getAll();

    OrderDetailResponse getById(Long id);

    OrderDetailResponse save(OrderDetailResponse order);

    OrderDetailResponse update(Long id, OrderDetailResponse order);

    boolean delete(Long id);
}
