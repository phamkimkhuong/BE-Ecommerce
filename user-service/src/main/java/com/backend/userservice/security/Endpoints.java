/*
 * @(#) $(NAME).java    1.0     2/27/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.userservice.security;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 27-February-2025 8:03 PM
 */

public class Endpoints {
    public static final String[] PUBLIC_GET_ENDPOINS = {
    };

    public static final String[] PUBLIC_POST_ENDPOINS = {
            "api/user/create"
    };

    public static final String[] PUBLIC_PUT_ENDPOINS = {
            "api/user/{id}",
    };

    public static final String[] ADMIN_GET_ENDPOINS = {
            "api/user",
            "api/user/**",
    };

    public static final String[] ADMIN_POST_ENDPOINS = {
            "api/user",
    };

    public static final String[] USER_GET_ENDPOINTS = {
            "api/user/**",
            "api/user/get-info",
    };

}
