package com.backend.productservice.controllers;


import com.backend.productservice.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/21/2025 1:07 PM
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Query", description = "Product API")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Get List Product",
            description = "Get all Product",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"),
            }
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> getUsers(@RequestParam(required = false) String keyword) {

        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("status", HttpStatus.OK.value());
        if (keyword == null || keyword.isEmpty()) {
            response.put("data", productService.getAllProduct());
        } else {
//            response.put("data", productService.search(keyword));
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
