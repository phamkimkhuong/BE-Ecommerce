package com.backend.productservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 21-February-2025 7:55 PM
 */

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @Column(name = "ten_sp", nullable = false,unique = true)
    private String tensp;
    @Column(name = "mo_ta",columnDefinition = "TEXT")
    private String moTa;
    @Column(name = "hinh_anh")
    private String hinhAnh;
    @Column(name = "gia_ban", nullable = false)
    private Double giaBan;
    @Column(name = "gia_nhap", nullable = false)
    private Double giaNhap;
    @Column(name = "gia_goc", nullable = false)
    private Double giaGoc;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductAttribute> productAttributes;
}
