package com.backend.orderservice.enums;
/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/24/2025 2:37 PM
 */

import com.backend.commonservice.enums.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus status) {
        if (status == null) {
            return null;
        }
        return status.getVietnameseLabel();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return OrderStatus.fromVietnameseLabel(dbData);
    }
}
