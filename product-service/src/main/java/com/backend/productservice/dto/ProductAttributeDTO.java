package com.backend.productservice.dto;

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
public class ProductAttributeDTO {
    @Schema(description = "Mã Thuộc Tính", hidden = true)
    Long id;
    @Positive(message = "Số lượng phải lớn hơn 0")
    int soLuong;
    @NotEmpty(message = "Màu sắc không được để trống")
    @Schema(description = "Mau sac sản phẩm", example = "Đen")
    String mauSac;
    @NotEmpty(message = "Kích cỡ không được để trống")
    @Schema(description = "Kích cỡ sản phẩm", example = "XL")
    String kichCo;
}
