package com.backend.productservice.dto;

import com.backend.productservice.domain.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    @NotEmpty(message = "Tên sản phẩm không được để trống")
    private String tensp;
    private String moTa;
    @NotEmpty(message = "Hình ảnh không được để trống")
    private String hinhAnh;
    @NotNull(message = "Giá bán không được để trống")
    private Double giaBan;
    @NotNull(message = "Giá nhập không được để trống")
    private Double giaNhap;
    @JsonIgnore
    private Category category;
}
