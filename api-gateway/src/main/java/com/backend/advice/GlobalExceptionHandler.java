package com.backend.advice;

import com.backend.model.AppException;
import com.backend.model.ErrorMessage;
import com.backend.response.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}