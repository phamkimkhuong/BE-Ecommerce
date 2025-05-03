package com.backend.orderservice.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    DANG_XU_LY("Đang xử lý"),
    CHUA_THANH_TOAN("Chưa thanh toán"),
    DA_THANH_TOAN("Đã thanh toán"),
    DA_GIAO("Đã giao"),
    DA_NHAN("Đã nhận"),
    HUY("Đã hủy");

    private final String vietnameseLabel;

    OrderStatus(String vietnameseLabel) {
        this.vietnameseLabel = vietnameseLabel;
    }

    // Phương thức tìm kiếm từ nhãn tiếng Việt
    public static OrderStatus fromVietnameseLabel(String label) {
        for (OrderStatus status : values()) {
            if (status.vietnameseLabel.equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái: " + label);
    }
}
