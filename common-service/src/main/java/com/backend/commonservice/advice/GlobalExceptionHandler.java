/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.commonservice.advice;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/21/2025 3:17 PM
 */

import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleAppException(AppException ex) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        ErrorMessage error = ex.getErrorCode();
        response.setCode(error.getCode());
        response.setMessage(error.getMessage());
        return new ResponseEntity<>(response, error.getHttpStatus());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ApiResponseDTO<Map<String, String>> response = new ApiResponseDTO<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            // Chuyển đổi tên trường từ camelCase sang snake_case
            fieldName = convertToSnakeCase(fieldName);
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        response.setCode(ErrorMessage.INVALID_DATA.getCode());
        response.setMessage(ErrorMessage.INVALID_DATA.getMessage());
        response.setErrors(errors);
       return  new ResponseEntity<>(response, ErrorMessage.INVALID_DATA.getHttpStatus());
    }

    // Exception này sẽ được xử lý khi người dùng không có quyền truy cập vào một tài nguyên nào đó
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> errors = new LinkedHashMap<>();
        ErrorMessage error = ErrorMessage.UNAUTHORIZED;
        errors.put("status", error.getHttpStatus());
        errors.put("message", error.getMessage());
        return new ResponseEntity<>(errors, error.getHttpStatus());
    }

    // Những lỗi mà không bắt được sẽ được xử lý ở đây
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errors.put("message", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private String convertToSnakeCase(String input) {
        return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

}
