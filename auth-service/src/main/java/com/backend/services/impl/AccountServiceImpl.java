/*
 * @(#) $(NAME).java    1.0     2/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.services.impl;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 26-February-2025 11:01 PM
 */

import com.backend.dtos.CreateUserRequest;
import com.backend.dtos.JwtResponse;
import com.backend.dtos.SignInRequest;
import com.backend.dtos.SignUpRequest;
import com.backend.entities.Account;
import com.backend.entities.Role;
import com.backend.repositories.AccountRepository;
import com.backend.repositories.RoleRepository;
import com.backend.services.AccountService;
import com.backend.services.EmailService;
import com.backend.services.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    @Autowired
    @Lazy
    private JWTService jwtService;

    @Autowired
    private EmailService emailService;


    @Autowired
    public AccountServiceImpl(ModelMapper modelMapper, RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder, AccountRepository accountRepository, RestTemplate restTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.restTemplate = restTemplate;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> signUp(SignUpRequest signUpRequest) {
        if (accountRepository.existsAccountByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken!");
        }
        if (accountRepository.existsAccountByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken!");
        }

        String encryptPassword = passwordEncoder.encode(signUpRequest.getPassword());
        signUpRequest.setPassword(encryptPassword);

        Account account = modelMapper.map(signUpRequest, Account.class);

        Role userRole = roleRepository.findRoleByName("USER");

        // Gán quyền USER cho tài khoản mới
        account.setRoles(Collections.singletonList(userRole));
        Account savedAccount = accountRepository.save(account); // lưu trước để có ID
        // Gọi user-service để tạo user trống
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setAccountId(savedAccount.getAccountId()); // dùng ID của account
        createUserRequest.setUsername(savedAccount.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateUserRequest> request = new HttpEntity<>(createUserRequest, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:8080/api/user/create", // Gọi qua API Gateway
                    request,
                    String.class);
            if (response.getStatusCode() != HttpStatus.CREATED) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Account created, but failed to create user profile.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Account created, but error calling user-service: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully!");
    }
    @Override
    public ResponseEntity<?> signIn(SignInRequest signInRequest, AuthenticationManager authenticationManager) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                final String token = jwtService.generateToken(signInRequest.getUsername());
                return ResponseEntity.ok(new JwtResponse(token));
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên đăng nhập hoặc mật khẩu không chính xác.");
        }
        return ResponseEntity.badRequest().body("Xác thực không thành công.");
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public String getEmailUser(Long accountId) {
        log.info("getEmailUser accountId: {}", accountId);
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        return account.getEmail();
    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        Account account = accountRepository.findAccountByEmail(email);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email không tồn tại trong hệ thống.");
        }

        // Tạo mật khẩu mới ngẫu nhiên
        String newPassword = generateRandomPassword(8); // Ví dụ: độ dài 8 ký tự
        String encodedPassword = passwordEncoder.encode(newPassword);

        // Cập nhật vào DB
        account.setPassword(encodedPassword);
        accountRepository.save(account);

        // Gửi email
        String subject = "Khôi phục mật khẩu tài khoản";
        String content = "<p>Xin chào <strong>" + account.getUsername() + "</strong>,</p>" +
                "<p>Bạn đã yêu cầu khôi phục mật khẩu.</p>" +
                "<p>Mật khẩu mới của bạn là: <strong>" + newPassword + "</strong></p>" +
                "<p>Vui lòng đăng nhập và đổi mật khẩu ngay sau đó.</p>";
        try {
            emailService.sendMessage("no-reply@webecommerce.vn", email, subject, content);
            return ResponseEntity.ok("Mật khẩu mới đã được gửi về email của bạn.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể gửi email: " + e.getMessage());
        }
    }

    private String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username);
        if (username == null) {
            throw new UsernameNotFoundException("Account not found");
        }
        return new User(account.getUsername(), account.getPassword(), rolesToAuthorities(account.getRoles()));
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
