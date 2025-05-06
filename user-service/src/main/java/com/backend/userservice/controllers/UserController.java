/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.userservice.controllers;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 13-February-2025 8:26 PM
 */

import com.backend.commonservice.dto.request.ApiResponseDTO;
import com.backend.userservice.dtos.CreateUserRequest;
import com.backend.userservice.dtos.UserDTO;
import com.backend.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RepositoryRestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/check-user/{id}")
    public ApiResponseDTO<Boolean> checkUserExit(@PathVariable Long id) {
        ApiResponseDTO<Boolean> response = new ApiResponseDTO<>();
        Boolean check = userService.existsByAccountId(id);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Kiểm tra người dùng thành công");
        response.setData(check);
        return response;
    }

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
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

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

    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        log.info("Full request details: {}", request.toString());
        // Kiểm tra nếu accountId là null
        if (request.getAccountId() == null) {
            log.error("AccountId is null in the request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AccountId is required");
        }

        userService.createUserRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully with accountId: " + request.getAccountId());
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult) {

        ApiResponseDTO<Map<String, Object>> response = new ApiResponseDTO<>();

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new LinkedHashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Dữ liệu không hợp lệ");
            response.setErrors(Map.of("validationErrors", errors));
            return ResponseEntity.badRequest().body(response);
        }

        // Gán userId từ path vào DTO
        userDTO.setUserId(id);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("user", userService.save(userDTO));

        response.setCode(HttpStatus.OK.value());
        response.setMessage("Cập nhật người dùng thành công");
        response.setData(data);

        return ResponseEntity.ok(response);
    }

}
