package iuh.fit.se.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.se.entities.Quyen;
import jakarta.validation.constraints.NotEmpty;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link iuh.fit.se.entities.NguoiDung}
 */
public class NguoiDungDTO implements Serializable {
    int maNguoiDung;
    @NotEmpty(message = "Tên người dùng không được để trống")
    String tenNguoiDung;
    String email;
    String soDienThoai;
    String diaChi;
    boolean gioiTinh;
    String tenDangNhap;
    String matKhau;
    @JsonIgnore
    List<Quyen> danhSachQuyen;

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public @NotEmpty(message = "Tên người dùng không được để trống") String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(@NotEmpty(message = "Tên người dùng không được để trống") String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

}