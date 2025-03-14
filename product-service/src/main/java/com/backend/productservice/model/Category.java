package com.backend.productservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE) // Set private level for all fields
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    Long id;
    @Column(name = "ten_loai", unique = true, nullable = false)
    String tenLoai;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Product> products;
}
