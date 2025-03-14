package com.backend.productservice.model;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 3/10/2025 9:17 PM
 */

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Table(name = "product_attribute")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE) // Set private level for all fields
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "so_luong", nullable = false)
    @ColumnDefault("0")
    int soLuong;
    @Column(name = "mau_sac", nullable = false)
    String mauSac;
    @Column(name = "kich_co", nullable = false)
    String kichco;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JsonBackReference
    @JoinColumn(name = "product_id")
    Product product;
}
