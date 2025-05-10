package com.backend.commonservice.configuration;

import com.backend.commonservice.configuration.openFeign.CustomErrorDecoder;
import feign.Contract;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

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
            String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                    .getRequest()
                    .getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                requestTemplate.header("Authorization", token);
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