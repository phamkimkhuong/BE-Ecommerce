package com.backend.productservice.dto;

import com.backend.productservice.domain.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Schema(name = "Product Model", description = "Product Model Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @Schema(description = "Mã sản phẩm", hidden = true)
    private Long id;
    @NotEmpty(message = "Tên sản phẩm không được để trống")
    @Schema(description = "Tên sản phẩm", example = "Iphone 12 Pro Max")
    private String tensp;
    @Schema(description = "Mô tả sản phẩm", example = "Điện thoại Iphone 12 Pro Max")
    private String moTa;
    @NotEmpty(message = "Hình ảnh không được để trống")
    @Schema(description = "Hình ảnh sản phẩm", example = "https://www.google.com.vn")
    private String hinhAnh;
    @NotNull(message = "Giá bán không được để trống")
    @Schema(description = "Giá bán sản phẩm", example = "30000000")
    private Double giaBan;
    @NotNull(message = "Giá nhập không được để trống")
    @Schema(description = "Giá nhập sản phẩm", example = "25000000")
    private Double giaNhap;
    @JsonIgnore
    private Category category;
}
