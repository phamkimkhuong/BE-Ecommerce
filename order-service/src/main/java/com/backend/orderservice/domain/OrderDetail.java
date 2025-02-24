package com.backend.orderservice.domain;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/23/2025 9:41 AM
 */

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id",nullable = false)
    private Long productId;
    @Column(name = "so_luong",nullable = false)
    private Integer soLuong;
    @Column(name = "gia_ban",nullable = false)
    private Double giaBan;
    @Column(name = "gia_goc",nullable = false)
    private Double giaGoc;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

}
