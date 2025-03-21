package com.backend.productservice.controllers;


import com.backend.productservice.dto.reponse.ProductReponse;
import com.backend.productservice.dto.request.ProductCreationRequest;
import com.backend.productservice.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * @description
 * @author: pham kim khuong
 * @version: 1.0
 * @created: 2/21/2025 1:07 PM
 */

@RestController
@RequestMapping("/internal/products")
@Tag(name = "Product Query", description = "Product API")
public class InternalProductController {
    private final ProductService productService;

    public InternalProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Add Product",
            description = "Save a Product to database",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"),
            }
    )
    @PostMapping
    public ResponseEntity<ProductReponse> saveProduct(@RequestPart(name = "request") @Valid
                                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                              description = "Product object that needs to be added to the store",
                                                              required = true,
                                                              content = @io.swagger.v3.oas.annotations.media.Content(
                                                                      mediaType = "application/json",
                                                                      schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductCreationRequest.class)
                                                              )
                                                      )

                                                      ProductCreationRequest request
            , @RequestPart(name = "hinhAnh") MultipartFile hinhAnh
    ) {
        ProductReponse p = productService.saveProduct(request, hinhAnh);
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete Product",
            description = "Delete a Product from database",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"),
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.deleteProduct(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Update Product",
            description = "Update a Product from database",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"),
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductReponse> updateProduct(@PathVariable Long id, @Valid @RequestBody
                                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                description = "Product object that needs to be updated to the store",
                                                                required = true,
                                                                content = @io.swagger.v3.oas.annotations.media.Content(
                                                                        mediaType = "application/json",
                                                                        schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductCreationRequest.class)
                                                                )
                                                        ) ProductCreationRequest productDTO
    ) {
        ProductReponse p = productService.updateProduct(id, productDTO);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }
}
