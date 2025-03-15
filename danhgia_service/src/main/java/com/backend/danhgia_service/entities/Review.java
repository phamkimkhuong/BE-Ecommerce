package com.backend.danhgia_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "ngay_danh_gia")
    private Date ngayDanhGia;  // Ngày đánh giá

    @Column(name = "so_sao")
    private int soSao;  // Số sao đánh giá (thường từ 1 đến 5)

    @Column(name = "khach_hang_id")
    private Long customer;  // Khách hàng thực hiện đánh giá, thay đổi kiểu từ `int` thành `Long`

    @Column(name = "san_pham_id")
    private Long product;  // Sản phẩm được đánh giá, thay đổi kiểu từ `int` thành `Long`

    @Column(name = "noi_dung")
    private String noiDung;  // Nội dung đánh giá

    // Getter và Setter sẽ được tạo tự động nhờ Lombok
    //Các getter v setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter và Setter cho `ngayDanhGia`
    public Date getNgayDanhGia() {
        return ngayDanhGia;
    }

    public void setNgayDanhGia(Date ngayDanhGia) {
        this.ngayDanhGia = ngayDanhGia;
    }

    // Getter và Setter cho `soSao`
    public int getSoSao() {
        return soSao;
    }

    public void setSoSao(int soSao) {
        this.soSao = soSao;
    }

    // Getter và Setter cho `customer`
    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    // Getter và Setter cho `product`
    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    // Getter và Setter cho `noiDung`
    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
}
