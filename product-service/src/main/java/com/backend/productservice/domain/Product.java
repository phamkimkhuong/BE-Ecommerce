package com.backend.productservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ten_sp", nullable = false)
    @NotEmpty(message = "Tên sản phẩm không được để trống")
    private String tensp;
    @Column(name = "mo_ta")
    private String moTa;
    @Column(name = "hinh_anh")
    @NotEmpty(message = "Hình ảnh không được để trống")
    private String hinhAnh;
    @Column(name = "gia_ban", nullable = false)
    @NotEmpty(message = "Giá bán không được để trống")
    private Double giaBan;
    @Column(name = "gia_nhap", nullable = false)
    @NotEmpty(message = "Giá nhập không được để trống")
    private Double giaNhap;
}
