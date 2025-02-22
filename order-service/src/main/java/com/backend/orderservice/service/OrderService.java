package com.backend.orderservice.service;

import com.backend.orderservice.dtos.OrderDTO;

import java.util.List;

public interface OrderService {
    public List<OrderDTO> getAll();
    public OrderDTO getById(Long id);
    public OrderDTO save(OrderDTO order);
    public OrderDTO update(Long id,OrderDTO order);
    public boolean delete(Long id);
}
