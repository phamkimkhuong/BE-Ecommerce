/*
 * @(#) $(NAME).java    1.0     2/27/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package iuh.fit.se.security;

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
    };

    public static final String[] PUBLIC_POST_ENDPOINS = {
            "/api/account/sign-up",
    };

    public static final String[] ADMIN_GET_ENDPOINS = {
            "/api/account",
            "/api/account/**",
    };

}
