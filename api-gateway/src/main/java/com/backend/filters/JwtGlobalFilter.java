/*
 * @(#) $(NAME).java    1.0     3/30/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.filters;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 30-March-2025 4:03 PM
 */

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.http.SecurityHeaders;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtGlobalFilter implements WebFilter {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    private String extractJwtFromRequest(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        // Bỏ qua xác thực với sign-up, sign-in
        if (path.equals("/api/account/sign-up") || path.equals("/api/account/sign-in")) {
            return chain.filter(exchange);
        }

        String token = extractJwtFromRequest(exchange);
        if (token == null) {
            return Mono.error(new JwtException("Missing or invalid Authorization header"));
        }
        System.out.println("Token: " + token);
        Claims claims = extractClaims(token);
        System.out.println("Claims: " + claims);
        List<SimpleGrantedAuthority> authorities = extractAuthoritiesFromClaims(claims);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                authorities
        );
        System.out.println("Authentication: " + authentication);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        return chain.filter(exchange);
    }

    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaims(Claims claims) {
        List<String> roles = claims.get("roles", List.class);
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
