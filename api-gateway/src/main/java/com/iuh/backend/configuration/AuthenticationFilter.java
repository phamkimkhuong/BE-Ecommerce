package com.iuh.backend.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter into Auth Filter...");
        // Get token from authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            log.info("Authorization header is empty or null...");
//            return unauthorized(exchange.getResponse());
        }
        // Check token is valid or not
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // -1 is the highest priority , filter will run first
    }

    Mono<Void> unauthorized(ServerHttpResponse exchange) {
        String message = "Unauthorized Request";
        exchange.setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.writeWith(
                Mono.just(exchange.bufferFactory().wrap(message.getBytes())));
    }
}
