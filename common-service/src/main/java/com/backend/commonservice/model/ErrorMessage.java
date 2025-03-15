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
    USER_NOT_FOUND("User not found", 404, HttpStatus.NOT_FOUND),
    ITEM_NOT_FOUND("Item not found", 404, HttpStatus.NOT_FOUND),
    ;

    String message;
    int code;
    HttpStatus httpStatus;


}
