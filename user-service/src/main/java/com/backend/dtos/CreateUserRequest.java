/*
 * @(#) $(NAME).java    1.0     4/12/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.dtos;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 12-April-2025 10:12 AM
 */

public class CreateUserRequest {

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
