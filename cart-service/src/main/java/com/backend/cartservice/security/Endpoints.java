/*
 * @(#) $(NAME).java    1.0     2/27/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.cartservice.security;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 27-February-2025 8:03 PM
 */

public class Endpoints {
        public static final String[] PUBLIC_GET_ENDPOINS = {
                        // Không có endpoint GET công khai
        };

        public static final String[] PUBLIC_POST_ENDPOINS = {
                        // Không có endpoint POST công khai
        };
        public static final String[] ADMIN_GET_ENDPOINS = {
                        "/api/carts/**", // Admin có thể xem tất cả giỏ hàng
                        "//api/cart-items/**", // Admin có thể xem tất cả các mục trong giỏ hàng
        };

        public static final String[] ADMIN_POST_ENDPOINS = {
                        "/api/carts/**",
                        "/api/cart-items/**",
        };
}
