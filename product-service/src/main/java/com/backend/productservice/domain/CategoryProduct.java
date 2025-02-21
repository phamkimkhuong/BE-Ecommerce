package com.backend.productservice.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table
@Entity
public class CategoryProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tenLoai;
}
