package com.backend.commonservice.enums;

import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;

public enum EventType {
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    CANCEL("CANCEL");
    private final String vietnameseLabel;

    EventType(String vietnameseLabel) {
        this.vietnameseLabel = vietnameseLabel;
    }

    // Phương thức tìm kiếm từ nhãn tiếng Việt
    public static EventType fromVietnameseLabel(String label) {
        for (EventType status : values()) {
            if (status.vietnameseLabel.equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new AppException(ErrorMessage.RESOURCE_NOT_FOUND, "Không tìm thấy trạng thái đơn hàng: {} " + label);
    }
}
