package com.backend.commonservice.configuration;

import com.backend.commonservice.configuration.openFeign.CustomErrorDecoder;
import com.backend.commonservice.model.TokenContext;
import feign.Contract;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@Slf4j
public class FeignClientConfig {

    /**
     * Truyền thông tin xác thực vào các yêu cầu Feign
     * Trả về một RequestInterceptor để thêm thông tin xác thực vào các yêu cầu Feign.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Thử lấy token từ RequestContextHolder (cho HTTP request thông thường)
            String token = null;
            try {
                ServletRequestAttributes requestAttributes =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes != null) {
                    token = requestAttributes.getRequest().getHeader("Authorization");
                    log.info("Token from RequestContextHolder: {}", token);
                }
                // LẤY ĐỊA CHỈ api REQUEST
            } catch (Exception e) {
                log.warn("Could not get token from RequestContextHolder", e);
            }
            // Nếu không có token từ RequestContextHolder, thử lấy từ nguồn khác (ThreadLocal riêng)
            if (token == null) {
                token = TokenContext.getToken();
                log.info("Token from TokenContext: {}", token);
            }
            // Thêm token vào header nếu tồn tại
            if (token != null && !token.isEmpty()) {
                requestTemplate.header("Authorization", token);
            } else {
                log.warn("No token available for Feign request");
            }
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Bean
    public Contract feignContract() {
        return new SpringMvcContract();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}