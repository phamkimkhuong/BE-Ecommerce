package com.backend.productservice.model;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 3/10/2025 9:17 PM
 */

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Table(name = "product_attribute")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "so_luong", nullable = false)
    @ColumnDefault("0")
    private int soLuong;
    @Column(name = "mau_sac", nullable = false)
    private String mauSac;
    @Column(name = "kich_co", nullable = false)
    private String kichco;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JsonBackReference
    @JoinColumn(name = "product_id")
    private Product product;
}
