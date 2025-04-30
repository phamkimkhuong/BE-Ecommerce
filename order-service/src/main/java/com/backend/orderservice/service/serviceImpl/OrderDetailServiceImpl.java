package com.backend.orderservice.service.serviceImpl;


/*
 * @description
 * @author: Pham Kim khuong
 * @version: 1.0
 * @created: 2/23/2025 12:55 PM
 */

import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import com.backend.orderservice.domain.OrderDetail;
import com.backend.orderservice.dtos.OrderDetailDTO;
import com.backend.orderservice.repository.OrderDetailRepository;
import com.backend.orderservice.service.OrderDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRep;
    private final ModelMapper modelMapper;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRep, ModelMapper modelMapper) {
        this.orderDetailRep = orderDetailRep;
        this.modelMapper = modelMapper;
    }

    //    Convert Entity to DTO
    public OrderDetail convertToEntity(OrderDetailDTO product) {
        return modelMapper.map(product, OrderDetail.class);
    }

    //    Convert DTO to Entity
    public OrderDetailDTO convertToDTO(OrderDetail product) {
        return modelMapper.map(product, OrderDetailDTO.class);
    }

    @Override
    public List<OrderDetailDTO> getAll() {
        return orderDetailRep.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public OrderDetailDTO getById(Long id) {
        return orderDetailRep.findById(id).map(this::convertToDTO).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
    }

    @Transactional
    @Override
    public OrderDetailDTO save(OrderDetailDTO product) {
        OrderDetail p = orderDetailRep.save(convertToEntity(product));
        return convertToDTO(p);
    }

    @Transactional
    @Override
    public OrderDetailDTO update(Long id, OrderDetailDTO product) {
        orderDetailRep.findById(id).map(this::convertToDTO).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
        OrderDetail p = orderDetailRep.save(convertToEntity(product));
        return convertToDTO(p);
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        orderDetailRep.deleteById(id);
        return true;
    }
}
