package com.backend.danhgia_service.entities;

import com.google.j2objc.annotations.Property;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.hc.core5.http.ProtocolVersionParser;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID của đánh giá

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_danh_gia", nullable = false)
    // Ngày đánh giá khoong thể nul
    private Date ngayDanhGia;  // Ngày đánh giá

    @Column(name = "so_sao")
    private int soSao;  // Số sao đánh giá (thường từ 1 đến 5)

    @Column(name = "khach_hang_id")
    @NotBlank(message = "Khách hàng không được để trống")
    private Long customer;  // Khách hàng thực hiện đánh giá, thay đổi kiểu từ `int` thành `Long`

    @Column(name = "san_pham_id")
    @NotBlank(message = "Sản phẩm không được để trống")
    private Long product;  // Sản phẩm được đánh giá, thay đổi kiểu từ `int` thành `Long`

    @Column(name = "noi_dung")
    @NotBlank(message = "Nội dung không được để trống")
    @Size(max = 500, message = "Nội dung không được vượt quá 500 ký tự")
    private String noiDung;  // Nội dung đánh giá

}
