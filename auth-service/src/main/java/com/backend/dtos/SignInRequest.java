/*
 * @(#) $(NAME).java    1.0     3/10/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.dtos;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 10-March-2025 3:00 PM
 */

import jakarta.validation.constraints.NotBlank;

public class SignInRequest {
    @NotBlank(message = "Username is required!")
    private String username;

    @NotBlank(message = "Password is required!")
    private String password;

    public @NotBlank(message = "Username is required!") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is required!") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Password is required!") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required!") String password) {
        this.password = password;
    }
}
