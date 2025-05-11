/*
 * @(#) $(NAME).java    1.0     2/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.controllers;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 26-February-2025 11:05 PM
 */

import com.backend.dtos.SignInRequest;
import com.backend.dtos.SignUpRequest;
import com.backend.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.Map;

@RepositoryRestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/account/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignUpRequest signUpRequest, BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        if (bindingResult.hasErrors()) {
            Map<String, Object> errors = new LinkedHashMap<String, Object>();

            bindingResult.getFieldErrors().stream().forEach(result -> {
                errors.put(result.getField(), result.getDefaultMessage());
            });

            System.out.println(bindingResult);
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("errors", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("data", accountService.signUp(signUpRequest));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
    @GetMapping("/account/get-email")
    public ResponseEntity<?> getEmailUser(@RequestParam(value = "id") Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", accountService.getEmailUser(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/account/sign-in")
    public ResponseEntity<?> signInUser(@RequestBody @Valid SignInRequest signInRequest, BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        if (bindingResult.hasErrors()) {
            Map<String, Object> errors = new LinkedHashMap<String, Object>();

            bindingResult.getFieldErrors().stream().forEach(result -> {
                errors.put(result.getField(), result.getDefaultMessage());
            });

            System.out.println(bindingResult);
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("errors", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("data", accountService.signIn(signInRequest, authenticationManager));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
