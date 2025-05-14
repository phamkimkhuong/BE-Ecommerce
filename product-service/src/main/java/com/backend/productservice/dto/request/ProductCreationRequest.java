package com.backend.productservice.dto.request;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 3/10/2025 10:24 PM
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
public class ProductCreationRequest {
    @Schema(description = "Mã sản phẩm", hidden = true)
    Long id;
    @JsonProperty("ten_sp")
    @NotEmpty(message = "Tên sản phẩm không được để trống")
    @Schema(description = "Tên sản phẩm", example = "Iphone 12 Pro Max")
    @Size(min = 3, max = 50, message = "Tên sản phẩm từ 3-50 ký tự")
    String tenSP;
    @Schema(description = "Mô tả sản phẩm", example = "Điện thoại Iphone 12 Pro Max")
            @JsonProperty("mo_ta")
    String moTa;
    @NotNull(message = "Giá bán không được để trống")
    @Schema(description = "Giá bán sản phẩm", example = "30000000")
    @Positive(message = "Giá bán phải lớn hơn 0")
    @JsonProperty("gia_ban")
    Double giaBan;
    @NotNull(message = "Giá nhập không được để trống")
    @Schema(description = "Giá nhập sản phẩm", example = "25000000")
    @Positive(message = "Giá nhập phải lớn hơn 0")
    @JsonProperty("gia_nhap")
    Double giaNhap;
    @Schema(description = "Giá gốc sản phẩm", example = "25000000")
    @NotNull(message = "Giá gốc không được để trống")
    @Positive(message = "Giá gốc phải lớn hơn 0")
    @JsonProperty("gia_goc")
    Double giaGoc;
    @Positive(message = "Số lượng phải lớn hơn 0")
    @JsonProperty("so_luong")
    int soLuong;
    @NotEmpty(message = "Màu sắc không được để trống")
    @Schema(description = "Mau sac sản phẩm", example = "Đen")
    @JsonProperty("mau_sac")
    String mauSac;
    @NotEmpty(message = "Kích cỡ không được để trống")
    @Schema(description = "Kích cỡ sản phẩm", example = "XL")
    @JsonProperty("kich_co")
    String kichCo;
    @NotNull(message = "Thương hiệu không được để trống")
    @Schema(description = "Thương hiệu sản phẩm", example = "1")
    Long category_id;
}
