package com.backend.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Schema(name = "Category Model", description = "Category Model Information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @Schema(description = "Mã Loai sản phẩm",hidden = true )
    private Long id;
    @NotEmpty(message = "Tên loại không được để trống")
    @Schema(description = "Tên loại", example = "Iphone")
    public String tenLoai;
}
