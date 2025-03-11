/*
 * @(#) $(NAME).java    1.0     2/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.dtos;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 26-February-2025 10:56 PM
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.backend.entities.Role;
import jakarta.validation.constraints.*;

import java.util.List;

public class SignUpRequest {
    @NotBlank(message = "Username is required!")
    @Size(min= 5, message = "Username must have at least 5 characters!")
    @Size(max= 20, message = "Username can have have at most 20 characters!")
    private String username;
    @NotBlank(message = "Password is required!")
    @Size(min = 8, message = "Password must have at least 8 characters!")
    @Size(max = 20, message = "Password can have have at most 20 characters!")
    private String password;
    @Email(message = "Email is not in valid format!")
    @NotBlank(message = "Email is required!")
    private String email;

    private List<Role> roles;

    public @NotBlank(message = "Username is required!") @Size(min = 5, message = "Username must have at least 5 characters!") @Size(max = 20, message = "Username can have have at most 20 characters!") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is required!") @Size(min = 5, message = "Username must have at least 5 characters!") @Size(max = 20, message = "Username can have have at most 20 characters!") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Password is required!") @Size(min = 8, message = "Password must have at least 8 characters!") @Size(max = 20, message = "Password can have have at most 20 characters!") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required!") @Size(min = 8, message = "Password must have at least 8 characters!") @Size(max = 20, message = "Password can have have at most 20 characters!") String password) {
        this.password = password;
    }

    public @Email(message = "Email is not in valid format!") @NotBlank(message = "Email is required!") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email is not in valid format!") @NotBlank(message = "Email is required!") String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
