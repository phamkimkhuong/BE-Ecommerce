package com.backend.productservice.dto;

import lombok.Data;

import java.security.Principal;

@Data
public class ProductDTO {
    private Long id;
    private String tensp;
    private String moTa;
    private String hinhAnh;
    private Double giaBan;
    private Double giaNhap;

}
