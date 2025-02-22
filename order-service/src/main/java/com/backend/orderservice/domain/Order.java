package com.backend.orderservice.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long id;
    @Column(name = "ngay_dat_hang")
    private LocalDate ngayDatHang;
    @Column(name = "tong_tien")
    private Double tongTien;
}
