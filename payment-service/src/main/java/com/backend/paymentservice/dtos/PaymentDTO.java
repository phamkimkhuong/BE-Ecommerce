/*
 * @(#) $(NAME).java    1.0     4/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.paymentservice.dtos;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 25-April-2025 11:05 PM
 */

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaymentDTO {
    public String code;
    public String message;
    public String paymentURL;
}
