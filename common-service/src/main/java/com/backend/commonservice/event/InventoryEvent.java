package com.backend.commonservice.event;

/*
 * @description: Model đại diện cho sự kiện cập nhật tồn kho
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2024-05-15
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class InventoryEvent {
    Long orderId;
    LocalDateTime timestamp;
    String eventType; // INVENTORY_RESERVED, INVENTORY_CONFIRMED, INVENTORY_RELEASED
    Boolean success;
    String errorMessage;
    List<InventoryItem> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InventoryItem {
        Long productId;
        Integer quantity;
    }

    // Factory method để tạo sự kiện cập nhật tồn kho thành công
    public static InventoryEvent inventoryReserved(Long orderId, List<InventoryItem> items) {
        return InventoryEvent.builder()
                .orderId(orderId)
                .timestamp(LocalDateTime.now())
                .eventType("INVENTORY_RESERVED")
                .success(true)
                .items(items)
                .build();
    }

    // Factory method để tạo sự kiện cập nhật tồn kho thất bại
    public static InventoryEvent inventoryFailed(Long orderId, List<InventoryItem> items, String errorMessage) {
        return InventoryEvent.builder()
                .orderId(orderId)
                .timestamp(LocalDateTime.now())
                .eventType("INVENTORY_FAILED")
                .success(false)
                .errorMessage(errorMessage)
                .items(items)
                .build();
    }

    // Factory method để tạo sự kiện hoàn trả tồn kho (compensating action)
    public static InventoryEvent inventoryReleased(Long orderId, List<InventoryItem> items) {
        return InventoryEvent.builder()
                .orderId(orderId)
                .timestamp(LocalDateTime.now())
                .eventType("INVENTORY_RELEASED")
                .success(true)
                .items(items)
                .build();
    }
}