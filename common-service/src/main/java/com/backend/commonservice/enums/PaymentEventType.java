package com.backend.commonservice.enums;

import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;

public enum PaymentEventType {
    PAYMENT_INITIATE("PAYMENT_INITIATE"),
    PAYMENT_SUCCESS("PAYMENT_SUCCESS"),
    PAYMENT_FAILURE("PAYMENT_FAILURE"),
    PAYMENT_REFUND("PAYMENT_REFUND"),
    PAYMENT_CANCEL("PAYMENT_CANCEL");
    private final String vietnameseLabel;

    PaymentEventType(String vietnameseLabel) {
        this.vietnameseLabel = vietnameseLabel;
    }

    // Phương thức tìm kiếm từ nhãn tiếng Việt
    public static PaymentEventType fromVietnameseLabel(String label) {
        for (PaymentEventType status : values()) {
            if (status.vietnameseLabel.equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new AppException(ErrorMessage.RESOURCE_NOT_FOUND, "Không tìm thấy trang thai thanh toan: {} " + label);
    }
}
