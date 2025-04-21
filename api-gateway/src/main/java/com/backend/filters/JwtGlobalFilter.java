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

import com.backend.model.ErrorMessage;
import com.backend.model.OpenApiEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

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
//    private final List<String> openApiEndpoints = Arrays.asList(
//            "/api/account/sign-up",
//            "/api/account/sign-in",
//            "/api/v1/products"
//    );
    private final List<OpenApiEndpoint> openApiEndpoints = Arrays.asList(
            new OpenApiEndpoint("POST", "/api/account/sign-up"),
            new OpenApiEndpoint("POST", "/api/account/sign-in"),
            new OpenApiEndpoint("GET", "/api/v1/products")
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Authentication filter is called");
        // Lấy đường dẫn của request
        String path = exchange.getRequest().getURI().getPath();

        // Kiểm tra nếu đường dẫn không yêu cầu xác thực
        String method = exchange.getRequest().getMethod().name();
        logger.info("Request method: {}, path: {}", method, path);
        if (isOpenEndpoint(method, path)) {
            return chain.filter(exchange);
        }
        // Trích xuất token từ header
        List<String> authHearde = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        // Nếu không có header Authorization, trả về lỗi
        if (CollectionUtils.isEmpty(authHearde)) {
            logger.info("Authentication failed: Missing Authorization header for path {}", path);
            return apiErrorResponse(
                    exchange,
                    HttpStatus.UNAUTHORIZED,
                    ErrorMessage.MISSING_TOKEN.getCode(),
                    ErrorMessage.MISSING_TOKEN.getMessage()
            );
        }

        String token = authHearde.getFirst().replaceFirst("Bearer ", "");
        if (token.trim().isEmpty()) {
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
            List<?> rawRoles = claims.get("roles", List.class);
            List<String> roles = rawRoles == null
                    ? Collections.emptyList()
                    : rawRoles.stream().map(String::valueOf).collect(Collectors.toList());

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

    // Kiểm tra xem đường dẫn có nằm trong danh sách các endpoint không yêu cầu xác thực hay không
    private boolean isOpenEndpoint(String method, String path) {
        return openApiEndpoints.stream()
                .anyMatch(endpoint -> endpoint.getMethod().equalsIgnoreCase(method)
                        && endpoint.getPath().equalsIgnoreCase(path));
    }

    // Trích xuất claims từ token
    private Claims extractClaims(String token) throws JwtException {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Kiểm tra xem token đã hết hạn hay chưa
    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.before(new Date());
    }

    private boolean hasAccess(String path, List<String> roles) {
        // Kiểm tra quyền truy cập cơ bản dựa trên đường dẫn và role
        // Lưu ý: Đây chỉ là kiểm tra cơ bản, kiểm tra quyền chi tiết nên được thực hiện ở service

        // Ví dụ: Chỉ admin mới có thể truy cập các API quản trị
//        if (path.startsWith("/api/admin") && !roles.contains("ROLE_ADMIN")) {
//            return false;
//        }

        return true;
    }


    private Mono<Void> apiErrorResponse(ServerWebExchange exchange, HttpStatus status, int code, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", new Date().toString());
        responseBody.put("code", code);
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