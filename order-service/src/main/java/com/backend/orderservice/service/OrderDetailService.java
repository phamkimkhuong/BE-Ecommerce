package com.backend.orderservice.service;

import com.backend.orderservice.dtos.OrderDetailDTO;

import java.util.List;

public interface OrderDetailService {
    public List<OrderDetailDTO> getAll();

    public OrderDetailDTO getById(Long id);

    public OrderDetailDTO save(OrderDetailDTO order);

    public OrderDetailDTO update(Long id, OrderDetailDTO order);

    public boolean delete(Long id);
}
