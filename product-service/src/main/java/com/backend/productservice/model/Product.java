package com.backend.productservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE) // Set private level for all fields
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    Long id;
    @Column(name = "ten_sp", nullable = false, unique = true)
    String tensp;
    @Column(name = "mo_ta", columnDefinition = "TEXT")
    String moTa;
    @Column(name = "hinh_anh")
    String hinhAnh;
    @Column(name = "gia_ban", nullable = false)
    Double giaBan;
    @Column(name = "gia_nhap", nullable = false)
    Double giaNhap;
    @Column(name = "gia_goc", nullable = false)
    Double giaGoc;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "category_id")
    Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    List<ProductAttribute> productAttributes;
}
