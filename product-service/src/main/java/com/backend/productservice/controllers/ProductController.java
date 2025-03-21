package com.backend.productservice.controllers;


import com.backend.productservice.dto.reponse.ProductReponse;
import com.backend.productservice.dto.request.ProductCreationRequest;
import com.backend.productservice.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * @description
 * author: pham kim khuong
 * @version: 1.0
 * @created: 2/21/2025 1:07 PM
 */

@Slf4j
@RestController
@RequestMapping("/products")
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
                            description = "Successful operation",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"),
            }
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> getProducts(@RequestParam(required = false) String keyword) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        if (keyword == null || keyword.isEmpty()) {
            response.put("data", productService.getAllProduct());
        } else {
//            response.put("data", productService.search(keyword));
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Add Product",
            description = "Save a Product to database with image upload",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"),
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductReponse> saveProduct(
            @RequestPart(name = "request") @Valid 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product object that needs to be added to the store",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProductCreationRequest.class)
                    )
            ) ProductCreationRequest request,
            
            @RequestPart(name = "hinhAnh") 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Image file to upload",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "multipart/form-data",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string", format = "binary")
                    )
            ) MultipartFile hinhAnh
    ) {
        log.info("Save Product Controller start...");
        log.info("request: {}", request);
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

    @Operation(
            summary = "Get Product by ID",
            description = "Get a Product by ID",
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
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.getProductById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
