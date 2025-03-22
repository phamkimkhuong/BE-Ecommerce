/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.productservice.exception;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/21/2025 3:17 PM
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandlerProduct {
    @ExceptionHandler(FileValidationException.class)
    public ResponseEntity<Map<String, Object>> imageUploadException(FileValidationException ex) {
        Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("message", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
