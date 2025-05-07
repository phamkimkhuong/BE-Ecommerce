package com.backend.commonservice.enums;

import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import lombok.Getter;

@Getter
public enum ThanhToanType {
    TT_KHI_NHAN_HANG("Thanh toán khi nhận hàng"),
    TT_VNPAY("Thanh toán qua VNPay"),
    TT_MOMO("Thanh toán qua Momo"),
    TT_NGAN_HANG("Thanh toán qua ngân hàng");
    private final String vietnameseLabel;

    ThanhToanType(String vietnameseLabel) {
        this.vietnameseLabel = vietnameseLabel;
    }

    // Phương thức tìm kiếm từ nhãn tiếng Việt
    public static ThanhToanType fromVietnameseLabel(String label) {
        for (ThanhToanType status : values()) {
            if (status.vietnameseLabel.equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new AppException(ErrorMessage.RESOURCE_NOT_FOUND, "Không tìm thấy hình thức thanh toán: {} " + label);
    }
}
