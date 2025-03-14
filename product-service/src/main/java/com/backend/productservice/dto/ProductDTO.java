package com.backend.productservice.dto;

import com.backend.productservice.model.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Schema(name = "Product Model", description = "Product Model Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    @Schema(description = "Mã sản phẩm", hidden = true)
    Long id;
    @NotEmpty(message = "Tên sản phẩm không được để trống")
    @Schema(description = "Tên sản phẩm", example = "Iphone 12 Pro Max")
    String tensp;
    @Schema(description = "Mô tả sản phẩm", example = "Điện thoại Iphone 12 Pro Max")
    String moTa;
    @NotEmpty(message = "Hình ảnh không được để trống")
    @Schema(description = "Hình ảnh sản phẩm", example = "https://www.google.com.vn")
    String hinhAnh;
    @NotNull(message = "Giá bán không được để trống")
    @Schema(description = "Giá bán sản phẩm", example = "30000000")
    @Positive(message = "Giá bán phải lớn hơn 0")
    Double giaBan;
    @NotNull(message = "Giá nhập không được để trống")
    @Schema(description = "Giá nhập sản phẩm", example = "25000000")
    @Positive(message = "Giá nhập phải lớn hơn 0")
    Double giaNhap;
    @Schema(description = "Giá gốc sản phẩm", example = "25000000")
    @NotNull(message = "Giá gốc không được để trống")
    @Positive(message = "Giá gốc phải lớn hơn 0")
    Double giaGoc;
    @Positive(message = "Số lượng phải lớn hơn 0")
    int soLuong;
    @NotEmpty(message = "Màu sắc không được để trống")
    @Schema(description = "Mau sac sản phẩm", example = "Đen")
    String mauSac;
    @NotEmpty(message = "Kích cỡ không được để trống")
    @Schema(description = "Kích cỡ sản phẩm", example = "XL")
    String kichCo;
}
