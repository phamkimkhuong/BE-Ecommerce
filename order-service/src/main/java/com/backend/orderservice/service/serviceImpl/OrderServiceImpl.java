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
import com.backend.orderservice.dtos.response.OrderResponse;
import com.backend.orderservice.enums.OrderStatus;
import com.backend.orderservice.event.OrderProducer;
import com.backend.orderservice.repository.OrderRepository;
import com.backend.orderservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository productRep;
    private final ModelMapper modelMapper;
    private final OrderProducer orderProducer;

    public OrderServiceImpl(OrderRepository productRep, ModelMapper modelMapper, OrderProducer orderProducer) {
        this.productRep = productRep;
        this.modelMapper = modelMapper;
        this.orderProducer = orderProducer;
    }

    //    Convert Entity to DTO
    public Order convertToEntity(OrderDTO product) {
        return modelMapper.map(product, Order.class);
    }

    //    Convert DTO to Entity
    public OrderResponse convertToDTO(Order product) {
        return modelMapper.map(product, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getAll() {
        return productRep.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public OrderResponse getById(Long id) {
        return productRep.findById(id).map(this::convertToDTO).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
    }

    @Transactional
    @Override
    public OrderResponse save(OrderDTO product) {
        Order order = convertToEntity(product);
        if (order.getNgayDatHang() == null) {
            order.setNgayDatHang(LocalDate.now());
        }
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.DANG_XU_LY);
        }
        Order savedOrder = productRep.save(order);
        
        // Gửi sự kiện đơn hàng mới đến Kafka
        orderProducer.sendOrderEvent(
                savedOrder.getId(),
                savedOrder.getCustomerId(),
                savedOrder.getStatus(),
                savedOrder.getTongTien());
                
        return convertToDTO(savedOrder);
    }

    @Transactional
    @Override
    public OrderResponse update(Long id, OrderDTO product) {
        Order existingOrder = productRep.findById(id).orElseThrow(
                () -> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));
        
        // Lưu trạng thái cũ để kiểm tra xem có thay đổi không
        OrderStatus oldStatus = existingOrder.getStatus();
        
        Order orderToUpdate = convertToEntity(product);
        orderToUpdate.setId(id);
        Order updatedOrder = productRep.save(orderToUpdate);
        
        // Nếu trạng thái đơn hàng thay đổi, gửi sự kiện đến Kafka
        if (updatedOrder.getStatus() != oldStatus) {
            orderProducer.sendOrderEvent(
                    updatedOrder.getId(),
                    updatedOrder.getCustomerId(),
                    updatedOrder.getStatus(),
                    updatedOrder.getTongTien());
        }
        
        return convertToDTO(updatedOrder);
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        productRep.deleteById(id);
        return true;
    }
}
