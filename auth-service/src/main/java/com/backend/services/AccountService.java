/*
 * @(#) $(NAME).java    1.0     2/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.services;


import com.backend.dtos.SignInRequest;
import com.backend.dtos.SignUpRequest;
import com.backend.entities.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 26-February-2025 10:53 PM
 */
public interface AccountService extends UserDetailsService {
     ResponseEntity<?> signUp(SignUpRequest account);
     ResponseEntity<?> signIn(SignInRequest account, AuthenticationManager authenticationManager);
     Account findByUsername(String username);
    String getEmailUser(Long id);
    ResponseEntity<?> forgotPassword(String email);
}
