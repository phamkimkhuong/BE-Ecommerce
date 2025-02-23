package com.backend.orderservice.domain;


import com.backend.orderservice.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

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
@Table
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ngay_dat_hang")
    @CreatedDate
    private LocalDate ngayDatHang;
    @Column(name = "tong_tien")
    private Double tongTien;
    @Column(name = "customer_id",nullable = false)
    private Long customerId;
    @Column(name = "trang_thai")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
