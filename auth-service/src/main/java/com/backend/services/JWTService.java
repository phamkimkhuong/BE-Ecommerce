/*
 * @(#) $(NAME).java    1.0     3/10/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.services;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 10-March-2025 3:06 PM
 */

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface JWTService {

    // Tạo JWT dựa trên tên đang nhập
    public String generateToken(String tenDangNhap);

    // Trích xuất thông tin cho 1 claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction);

    // Kiểm tra thời gian hết hạn từ JWT
    public Date extractExpiration(String token);

    // Kiểm tra thời gian hết hạn từ JWT
    public String extractUsername(String token);

    // Kiểm tra tính hợp lệ
    public Boolean validateToken(String token, UserDetails userDetails);

    public Claims extractAllClaims(String token);
}
