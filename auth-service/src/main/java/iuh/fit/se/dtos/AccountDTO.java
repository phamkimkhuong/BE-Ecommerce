/*
 * @(#) $(NAME).java    1.0     2/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package iuh.fit.se.dtos;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 26-February-2025 10:56 PM
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.se.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

public class AccountDTO {
    private int accountId;
    @NotNull(message = "Username is required")
    @NotEmpty(message = "Username is required")
    @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters")
    private String username;
    @NotNull(message = "Password is required")
    @NotEmpty(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    private String password;
    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
    @JsonIgnore
    private List<Role> roles;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public @NotNull(message = "Username is required") @NotEmpty(message = "Username is required") @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters") String getUsername() {
        return username;
    }

    public void setUsername(@NotNull(message = "Username is required") @NotEmpty(message = "Username is required") @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters") String username) {
        this.username = username;
    }

    public @NotNull(message = "Password is required") @NotEmpty(message = "Password is required") @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Password is required") @NotEmpty(message = "Password is required") @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters") String password) {
        this.password = password;
    }

    public @NotNull(message = "Email is required") @NotEmpty(message = "Email is required") @Email(message = "Email is invalid") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Email is required") @NotEmpty(message = "Email is required") @Email(message = "Email is invalid") String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
