package com.backend.productservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

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
@FieldDefaults(level = AccessLevel.PRIVATE) // Set private level for all fields
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    Long id;
    @Column(name = "ten_sp", nullable = false, unique = true)
    String tenSP;
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
    @Column(name = "so_luong", nullable = false)
    @ColumnDefault("0")
    int soLuong;
    @Column(name = "mau_sac", nullable = false)
    String mauSac;
    @Column(name = "kich_co", nullable = false)
    String kichCo;
    @Version
    @Column(name = "version", nullable = false, columnDefinition = "bigint default 0")
    private long version; // Field dùng cho optimistic locking

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id")
    Category category;
}
