package com.backend.commonservice.dto.reponse;


import lombok.*;
import lombok.experimental.FieldDefaults;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 3/10/2025 10:24 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE) // Set private level for all fields
//@JsonInclude(JsonInclude.Include.NON_NULL) // Ignore all null fields when return response
public class ProductReponse {
    Long id;
    String tensp;
    String moTa;
    String hinhAnh;
    Double giaBan;
    Double giaNhap;
    Double giaGoc;
    int soLuong;
    String mauSac;
    String kichCo;
}
