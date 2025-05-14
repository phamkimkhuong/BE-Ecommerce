/*
 * @(#) $(NAME).java    1.0     3/11/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.filter;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 11-March-2025 8:19 PM
 */

import com.backend.services.AccountService;
import com.backend.services.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper; // Dùng để parse danh sách roles

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("doFilterInternal method called =====================================");
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Authorization header is missing or does not start with Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username;

        try {
            username = jwtService.extractUsername(token);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT token");
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("Username: " + username);
            UserDetails userDetails = accountService.loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)) {
                Claims claims = jwtService.extractAllClaims(token);
                List<String> roles = objectMapper.convertValue(claims.get("roles"), List.class);
                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("SecurityContextHolder set with authentication");
            } else {
                logger.warn("Invalid or expired JWT token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired JWT token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}


