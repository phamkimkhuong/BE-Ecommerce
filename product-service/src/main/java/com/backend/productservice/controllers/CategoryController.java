package com.backend.productservice.controllers;


import com.backend.productservice.dto.CategoryDTO;
import com.backend.productservice.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/21/2025 5:33 PM
 */

@RestController
@RequestMapping("/api/v1/category")
@Tag(name = "Category Query", description = "Category API")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation
            (
                    summary = "Get List Category of Product",
                    description = "Get all Category of Product from database",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation"),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Category not found"),
                            @ApiResponse(
                                    responseCode = "500",
                                    description = "Internal server error"),
                    }
            )
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCategories(@RequestParam(required = false) String keyword) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        if (keyword == null || keyword.isEmpty()) {
            response.put("data", categoryService.getAll());
        } else {
//            response.put("data", productService.search(keyword));
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation
            (
                    summary = "Add Category",
                    description = "Save a Category to database",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation"),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Category not found"),
                            @ApiResponse(
                                    responseCode = "500",
                                    description = "Internal server error"),
                    }
            )
    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(@Valid @RequestBody CategoryDTO category
    ) {
        CategoryDTO c = categoryService.save(category);
        return new ResponseEntity<>(c, HttpStatus.CREATED);
    }

    @Operation
            (
                    summary = "Delete Category",
                    description = "Delete a Category from database",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation"),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Category not found"),
                            @ApiResponse(
                                    responseCode = "500",
                                    description = "Internal server error"),
                    }
            )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", categoryService.delete(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation
            (
                    summary = "Update Category",
                    description = "Update a Category from database",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation"),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Category not found"),
                            @ApiResponse(
                                    responseCode = "500",
                                    description = "Internal server error"),
                    }
            )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody CategoryDTO category
    ) {
        CategoryDTO c = categoryService.update(id, category);
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @Operation
            (
                    summary = "Get Category by ID",
                    description = "Get a Category by ID from database",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation"),
                            @ApiResponse(
                                    responseCode = "404",
                                    description = "Category not found"),
                            @ApiResponse(
                                    responseCode = "500",
                                    description = "Internal server error"),
                    }
            )
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", categoryService.getById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
