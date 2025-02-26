package iuh.fit.se.dtos;

import iuh.fit.se.entities.NguoiDung;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link iuh.fit.se.entities.Quyen}
 */
public class QuyenDTO implements Serializable {
    int maQuyen;
    String tenQuyen;
    List<NguoiDung> danhSachNguoiDung;

    public int getMaQuyen() {
        return maQuyen;
    }

    public void setMaQuyen(int maQuyen) {
        this.maQuyen = maQuyen;
    }

    public String getTenQuyen() {
        return tenQuyen;
    }

    public void setTenQuyen(String tenQuyen) {
        this.tenQuyen = tenQuyen;
    }
}