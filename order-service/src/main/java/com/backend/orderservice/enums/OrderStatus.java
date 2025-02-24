package com.backend.orderservice.enums;

public enum OrderStatus {
    CHO("Đang chờ"),
    DANG_XU_LY("Đang xử lý"),
    DA_GIAO("Đã giao"),
    DELIVERED("Đã nhận"),
    HUY("Đã hủy");

    private final String vietnameseLabel;

    OrderStatus(String vietnameseLabel) {
        this.vietnameseLabel = vietnameseLabel;
    }

    public String getVietnameseLabel() {
        return vietnameseLabel;
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
