package com.backend.commonservice.model;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/22/2025 7:38 PM
 */

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorMessage {
    // Lỗi không xác định

    USER_NOT_FOUND("Không tìm thấy người dùng", 404, HttpStatus.NOT_FOUND),
    RESOURCE_NOT_FOUND("Không tìm thấy tài nguyên", 404, HttpStatus.NOT_FOUND),
    UNAUTHORIZED("Bạn không có quyền truy cập tài nguyên này", 403, HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED("You are not authenticated", 401, HttpStatus.UNAUTHORIZED),
    INVALID_REQUEST("Invalid Request", 400, HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("Internal Server Error", 500, HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_CREDENTIAL("Invalid Credential", 400, HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("Token không hợp lệ", 400, HttpStatus.BAD_REQUEST),
    USER_SERVER_ERROR("Dịch vụ khách hàng lỗi", 500, HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_SERVER_ERROR("Dịch vụ sản phẩm lỗi", 500, HttpStatus.INTERNAL_SERVER_ERROR),
    CART_SERVER_ERROR("Dịch vụ giỏ hàng lỗi", 500, HttpStatus.INTERNAL_SERVER_ERROR),
    ORDER_SERVER_ERROR("Dịch vụ đơn hàng lỗi", 500, HttpStatus.INTERNAL_SERVER_ERROR),
    PAYMENT_SERVER_ERROR("Dịch vụ thanh toán lỗi", 500, HttpStatus.INTERNAL_SERVER_ERROR),
    AUTH_SERVER_ERROR("Dịch vụ xác thực lỗi", 500, HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("Bad Request", 400, HttpStatus.BAD_REQUEST),
    INVALID_DATA("Validation failed", 400, HttpStatus.BAD_REQUEST),
    DUPLICATE_DATA("Duplicate Data", 400, HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER("Invalid Parameter", 400, HttpStatus.BAD_REQUEST),
    // Không tìm thấy sản phẩm
    PRODUCT_NOT_FOUND("Không tìm thấy thông tin sản phẩm", 404, HttpStatus.NOT_FOUND),
    // Số lượng sản phẩm không đủ
    PRODUCT_QUANTITY_NOT_ENOUGH("Số lượng sản phẩm không đủ", 421, HttpStatus.BAD_REQUEST),
    // Không tìm thấy sản phẩm trong giỏ hàng
    CART_ITEM_NOT_FOUND("Không tìm thấy mục sản phẩm trong giỏ hàng", 404, HttpStatus.NOT_FOUND),
    // Không tìm thấy giỏ hàng
    CART_NOT_FOUND("Không tìm thấy giỏ hàng", 404, HttpStatus.NOT_FOUND),
    // Giỏ hàng đã tồn tại cho khách hàng này
    CART_ALREADY_EXISTS("Giỏ hàng đã tồn tại cho khách hàng này", 400, HttpStatus.BAD_REQUEST),
    // Thanh toán thất bại
    PAYMENT_FAILED("Thanh toán thất bại", 400, HttpStatus.BAD_REQUEST),
    // Cập nhật tồn kho thất bại
    INVENTORY_UPDATE_FAILED("Cập nhật tồn kho thất bại", 400, HttpStatus.BAD_REQUEST),
    // Kafka Lỗi
    KAFKA_ERROR("Lỗi dịch v Kafka", 500, HttpStatus.INTERNAL_SERVER_ERROR),
    // Dich vu xac thuc loi
    ;

    String message;
    int code;
    HttpStatus httpStatus;

}
