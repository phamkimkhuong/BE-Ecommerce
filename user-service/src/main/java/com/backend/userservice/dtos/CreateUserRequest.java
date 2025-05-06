/*
 * @(#) $(NAME).java    1.0     4/12/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 12-April-2025 10:12 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private Long userId;
    @NotBlank(message = "Tên tài khoản không được để trống")
    private String username;
    @NotNull(message = "Mã tài khoản không được để trống")
    @JsonProperty("account_id")
    private Long accountId;
}
