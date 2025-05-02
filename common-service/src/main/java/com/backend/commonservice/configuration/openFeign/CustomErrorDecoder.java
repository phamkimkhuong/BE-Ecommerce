package com.backend.commonservice.configuration.openFeign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try (InputStream responseBody = response.body().asInputStream()) {
            // Cố gắng đọc nội dung response để xử lý lỗi
            if (responseBody != null) {
                String responseBodyString = new String(responseBody.readAllBytes());
                log.error("Feign client error: {} - {}", response.status(), responseBodyString);

                // Trả về exception có thông tin chi tiết từ response body
                return new FeignResponseException(response.status(), responseBodyString);
            }
        } catch (IOException e) {
            log.error("Error reading response body", e);
        }

        // Fallback to default error decoder
        return defaultErrorDecoder.decode(methodKey, response);
    }
}