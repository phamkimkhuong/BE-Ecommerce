/*
 * @(#) $(NAME).java    1.0     4/12/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 12-April-2025 10:03 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String username;
    @JsonProperty("account_id")
    private Long accountId;
}
