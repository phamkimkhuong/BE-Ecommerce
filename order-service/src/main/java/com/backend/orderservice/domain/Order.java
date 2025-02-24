package com.backend.orderservice.domain;


import com.backend.orderservice.enums.OrderStatus;
import com.backend.orderservice.enums.OrderStatusConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.List;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/22/2025 10:48 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ngay_dat_hang",nullable = false)
    @CreatedDate
    private LocalDate ngayDatHang;
    @Column(name = "tong_tien",nullable = false)
    private Double tongTien;
    @Column(name = "trang_thai",nullable = false)
  @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status;
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

}
