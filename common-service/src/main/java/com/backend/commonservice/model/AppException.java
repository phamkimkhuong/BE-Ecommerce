package com.backend.commonservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    public AppException (ErrorMessage errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    private ErrorMessage errorCode;
}
