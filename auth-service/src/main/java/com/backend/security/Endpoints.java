/*
 * @(#) $(NAME).java    1.0     2/27/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.security;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 27-February-2025 8:03 PM
 */

public class Endpoints {
    public static final String[] PUBLIC_GET_ENDPOINS = {
            "/api/account/search/existsByTenDangNhap",
            "/api/account/search/existsByEmail",
            "/swagger-ui/**",      // Cho phép Swagger UI
            "/v3/api-docs/**",     // Cho phép OpenAPI
            "/swagger-resources/**",
            "/webjars/**",
    };

    public static final String[] PUBLIC_POST_ENDPOINS = {
            "/api/account/sign-up",
            "/api/account/sign-in",
            "/api/account/forgot-password",
    };

    public static final String[] ADMIN_GET_ENDPOINS = {
            "/api/account",
            "/api/account/**",
    };
}
