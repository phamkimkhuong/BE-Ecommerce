package com.backend.cartservice.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    @NotNull(message = "Mã khách hàng không được rỗng")
    @JsonProperty(value = "customer_id")
    private Long customerId;
}