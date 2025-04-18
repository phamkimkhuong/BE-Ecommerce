package com.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpenApiEndpoint {
    private final String method;
    private final String path;
}
