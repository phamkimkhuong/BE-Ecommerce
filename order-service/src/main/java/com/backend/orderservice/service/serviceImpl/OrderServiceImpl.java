package com.backend.orderservice.service.serviceImpl;


/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/21/2025 12:55 PM
 */

import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import com.backend.orderservice.domain.Order;
import com.backend.orderservice.dtos.OrderDTO;
import com.backend.orderservice.repository.OrderRepository;
import com.backend.orderservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository productRep;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository productRep, ModelMapper modelMapper) {
        this.productRep = productRep;
        this.modelMapper = modelMapper;
    }

    //    Convert Entity to DTO
    public Order convertToEntity(OrderDTO product) {
        return modelMapper.map(product, Order.class);
    }

    //    Convert DTO to Entity
    public OrderDTO convertToDTO(Order product) {
        return modelMapper.map(product, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> getAll() {
        return productRep.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public OrderDTO getById(Long id) {
        return productRep.findById(id).map(this::convertToDTO).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
    }

    @Transactional
    @Override
    public OrderDTO save(OrderDTO product) {
        Order p = productRep.save(convertToEntity(product));
        return convertToDTO(p);
    }

    @Transactional
    @Override
    public OrderDTO update(Long id, OrderDTO product) {
        productRep.findById(id).map(this::convertToDTO).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
        Order p = productRep.save(convertToEntity(product));
        return convertToDTO(p);
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        productRep.deleteById(id);
        return true;
    }
}
