package com.backend.commonservice.configuration.openFeign;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    /*
     *
     * Phương thức này được sử dụng để giải mã lỗi từ phản hồi của Feign client.
     * Nó sẽ cố gắng đọc nội dung của phản hồi và tạo ra một ngoại lệ tùy chỉnh nếu có thông tin chi tiết về lỗi.
     * Nếu không, nó sẽ trả về một ngoại lệ mặc định.
     *
     * */
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