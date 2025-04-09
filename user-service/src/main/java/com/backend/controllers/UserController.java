/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.controllers;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 13-February-2025 8:26 PM
 */

import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.backend.dtos.UserDTO;
import com.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedHashMap;
import java.util.Map;

@RepositoryRestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getUserById(@PathVariable Long id) {
        ApiResponseDTO<Map<String, Object>> response = new ApiResponseDTO<>();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("user", userService.findById(id));

        response.setCode(HttpStatus.OK.value());
        response.setMessage("Lấy thông tin người dùng thành công");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getAllUsers() {
        ApiResponseDTO<Map<String, Object>> response = new ApiResponseDTO<>();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("users", userService.findAll());

        response.setCode(HttpStatus.OK.value());
        response.setMessage("Lấy danh sách người dùng thành công");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/user")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> saveUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult) {

        ApiResponseDTO<Map<String, Object>> response = new ApiResponseDTO<>();

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new LinkedHashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });

            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Dữ liệu không hợp lệ");
            response.setErrors(Map.of("validationErrors", errors));
            return ResponseEntity.badRequest().body(response);
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("user", userService.save(userDTO));

        response.setCode(HttpStatus.OK.value());
        response.setMessage("Tạo người dùng thành công");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

}
