package com.backend.model;


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
    RESOURCE_NOT_FOUND("Không tìm thấy tài nguyên", 404, HttpStatus.NOT_FOUND),
    // Token hêt hạn
    EXPIRED_TOKEN("Token đã hết hạn", 401, HttpStatus.UNAUTHORIZED),
    // KHông có token
    MISSING_TOKEN("Token không được cung cấp", 401, HttpStatus.UNAUTHORIZED),
    // Token không đúng định dạng
    INVALID_TOKEN("Token không đúng định dạng", 401, HttpStatus.UNAUTHORIZED),
    // CHữ ký token không hợp lệ
    INVALID_SIGNATURE("Chữ ký token không hợp lệ", 401, HttpStatus.UNAUTHORIZED),
    // Token không hợp lệ
    INVALID_JWT("Token không hợp lệ", 401, HttpStatus.UNAUTHORIZED),
    // Token không có quyền truy cập
    ACCESS_DENIED("Token không có quyền truy cập", 403, HttpStatus.FORBIDDEN),
    ;
    String message;
    int code;
    HttpStatus httpStatus;


}
