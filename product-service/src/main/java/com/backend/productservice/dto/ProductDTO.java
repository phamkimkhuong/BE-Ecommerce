package com.backend.productservice.dto;

import com.backend.productservice.domain.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String tensp;
    private String moTa;
    private String hinhAnh;
    private Double giaBan;
    private Double giaNhap;
    @JsonIgnore
    private Category category;
}
