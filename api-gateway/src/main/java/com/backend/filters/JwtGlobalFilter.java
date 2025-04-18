/*
 * @(#) JwtGlobalFilter.java    1.0     4/12/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.filters;

/*
 * @description: Filter xác thực JWT cho API Gateway
 * @author: Tran Tan Dat (Chỉnh sửa)
 * @version: 1.1
 * @created: 12-April-2025
 */

import com.backend.model.AppException;
import com.backend.model.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public int getOrder() {
        return -1; // độ ưu tiên cao
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtGlobalFilter.class);
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Danh sách các đường dẫn không cần xác thực
    private final List<String> openApiEndpoints = Arrays.asList(
            "/api/account/sign-up",
            "/api/account/sign-in",
            "/api/v1/products"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Kiểm tra nếu đường dẫn không yêu cầu xác thực
        if (isOpenEndpoint(path)) {
            return chain.filter(exchange);
        }

        // Trích xuất token từ header
        String token = extractJwtFromRequest(exchange);
        if (token == null) {
            logger.warn("Authentication failed: Missing token for path {}", path);
            return apiErrorResponse(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    ErrorMessage.MISSING_TOKEN.getCode(),
                    ErrorMessage.MISSING_TOKEN.getMessage()
            );
        }

        try {
            // Xác thực token và kiểm tra hạn sử dụng
            Claims claims = extractClaims(token);

            // Kiểm tra thời hạn token
            if (isTokenExpired(claims)) {
                logger.warn("Authentication failed: Token expired for user {}", claims.getSubject());
                return apiErrorResponse(
                        exchange,
                        HttpStatus.UNAUTHORIZED,
                        ErrorMessage.EXPIRED_TOKEN.getCode(),
                        ErrorMessage.EXPIRED_TOKEN.getMessage()
                );
            }

            // Lấy thông tin roles từ token
            List<String> roles = claims.get("roles", List.class);

            // Kiểm tra quyền truy cập cơ bản
            if (!hasAccess(path, roles)) {
                logger.warn("Authorization failed: Insufficient privileges for user {} to access {}",
                        claims.getSubject(), path);
                return apiErrorResponse(
                        exchange,
                        HttpStatus.FORBIDDEN,
                        ErrorMessage.ACCESS_DENIED.getCode(),
                        ErrorMessage.ACCESS_DENIED.getMessage()
                );
            }

            logger.info("Successfully authenticated user: {} with roles: {}", claims.getSubject(), roles);

            // Chuyển tiếp thông tin người dùng trong header đến các service phía sau
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(builder -> builder
                            .header("X-Auth-UserId", claims.getSubject())
                            .header("X-Auth-Roles", String.join(",", roles))
                            .header("X-Auth-Token", token) // Chuyển tiếp token gốc
                    )
                    .build();

            // Tiếp tục chuỗi filter
            return chain.filter(modifiedExchange);

        } catch (ExpiredJwtException e) {
            logger.warn("Authentication failed: Token expired");
            return apiErrorResponse(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    ErrorMessage.EXPIRED_TOKEN.getCode(),
                    ErrorMessage.EXPIRED_TOKEN.getMessage()
            );
        } catch (JwtException e) {
            logger.warn("Authentication failed: Invalid token - {}", e.getMessage());
            return apiErrorResponse(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    ErrorMessage.INVALID_TOKEN.getCode(),
                    ErrorMessage.INVALID_TOKEN.getMessage()
            );
        } catch (Exception e) {
            logger.error("Error processing request", e);
            return apiErrorResponse(
                    exchange,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    5000,
                    "Lỗi hệ thống: " + e.getMessage()
            );
        }
    }

    private boolean isOpenEndpoint(String path) {
        return openApiEndpoints.stream().anyMatch(path::startsWith);
    }

    private String extractJwtFromRequest(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Claims extractClaims(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.before(new Date());
    }

    private boolean hasAccess(String path, List<String> roles) {
        // Kiểm tra quyền truy cập cơ bản dựa trên đường dẫn và role
        // Lưu ý: Đây chỉ là kiểm tra cơ bản, kiểm tra quyền chi tiết nên được thực hiện ở service

        // Ví dụ: Chỉ admin mới có thể truy cập các API quản trị
        if (path.startsWith("/api/admin") && !roles.contains("ROLE_ADMIN")) {
            return false;
        }

        return true;
    }

    private Mono<Void> apiErrorResponse(ServerWebExchange exchange, HttpStatus status, int code, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", new Date().toString());
        responseBody.put("code", code);
        responseBody.put("status", status.value());
        responseBody.put("message", message);

        try {
            byte[] bytes = objectMapper.writeValueAsString(responseBody).getBytes(StandardCharsets.UTF_8);
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
            );
        } catch (Exception e) {
            logger.error("Error writing response", e);
            byte[] bytes = ("{\"code\":" + code + ",\"message\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
            );
        }
    }
}