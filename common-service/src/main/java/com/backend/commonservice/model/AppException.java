package com.backend.commonservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    public AppException(ErrorMessage errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorMessage errorCode, String addContent) {
        super(errorCode.getMessage() + " " + addContent);
        this.errorCode = errorCode;
        this.addContent = addContent;
    }

    private ErrorMessage errorCode;
    private String addContent;
}
