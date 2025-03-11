/*
 * @(#) $(NAME).java    1.0     3/10/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.services.impl;

import com.backend.services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 10-March-2025 3:28 PM
 */
@Component
public class JWTServiceImpl implements JWTService {

    public static final String SERECT = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @Override
    public String generateToken(String tenDangNhap) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("isAdmin", true);
        claims.put("x", "ABC");
        return createToken(claims, tenDangNhap);

    }

    // Tạo JWT với các claim đã chọn
    private String createToken(Map<String, Object> claims, String tenDangNhap) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(tenDangNhap)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // JWT hết hạn sau 30 phút
                .signWith(SignatureAlgorithm.HS256, getSigneKey())
                .compact();
    }

    // Lấy serect key
    private Key getSigneKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SERECT);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Trích xuất thông tin
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigneKey()).parseClaimsJws(token).getBody();
    }


    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm tra cái JWT đã hết hạn
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String tenDangNhap = extractUsername(token);
        System.out.println(tenDangNhap);
        return (tenDangNhap.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
