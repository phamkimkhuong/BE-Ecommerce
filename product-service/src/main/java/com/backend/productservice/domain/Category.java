package com.backend.productservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 20-February-2025 7:55 PM
 */
@Data
@Table(name = "category")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(name = "ten_loai")
    @NotEmpty(message = "Tên loại không được để trống")
    private String tenLoai;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
}
