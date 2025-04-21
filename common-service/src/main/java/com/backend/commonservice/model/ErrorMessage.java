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
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorMessage {
    USER_NOT_FOUND("Không tìm thấy người dùng", 404, HttpStatus.NOT_FOUND),
    RESOURCE_NOT_FOUND("Không tìm thấy tài nguyên", 404, HttpStatus.NOT_FOUND),
    UNAUTHORIZED("Bạn không có quyền truy cập tài nguyên này", 403, HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED("You are not authenticated", 401, HttpStatus.UNAUTHORIZED),
    INVALID_REQUEST("Invalid Request", 400, HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("Internal Server Error", 500, HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_CREDENTIAL("Invalid Credential", 400, HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("Token không hợp lệ", 400, HttpStatus.BAD_REQUEST),

    BAD_REQUEST("Bad Request", 400, HttpStatus.BAD_REQUEST),
    INVALID_DATA("Validation failed", 400, HttpStatus.BAD_REQUEST),
    DUPLICATE_DATA("Duplicate Data", 400, HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER("Invalid Parameter", 400, HttpStatus.BAD_REQUEST)
    ;
    String message;
    int code;
    HttpStatus httpStatus;


}
