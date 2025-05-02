package com.backend.commonservice.configuration.openFeign;

import lombok.Getter;

@Getter
public class FeignResponseException extends RuntimeException {
    private final int status;
    private final String responseBody;

    public FeignResponseException(int status, String responseBody) {
        super("HTTP " + status + " Error: " + responseBody);
        this.status = status;
        this.responseBody = responseBody;
    }

}