/*
 * @(#) $(NAME).java    1.0     2/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package iuh.fit.se.services.impl;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 26-February-2025 11:01 PM
 */

import iuh.fit.se.dtos.AccountDTO;
import iuh.fit.se.entities.Account;
import iuh.fit.se.entities.Role;
import iuh.fit.se.repositories.AccountRepository;
import iuh.fit.se.services.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private AccountDTO convertToDTO(Account account) {
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        return accountDTO;
    }

    private Account convertToEntity(AccountDTO accountDTO) {
        Account account = modelMapper.map(accountDTO, Account.class);
        return account;
    }

    public ResponseEntity<?> signUp(AccountDTO accountDTO) {
        if (accountRepository.existsAccountByUsername(accountDTO.getUsername())) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken!");
        }
        if (accountRepository.existsAccountByEmail(accountDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken!");
        }

        String encryptPassword = passwordEncoder.encode(accountDTO.getPassword());
        accountDTO.setPassword(encryptPassword);

        Account account = convertToEntity(accountDTO);
        accountRepository.save(account);
        return ResponseEntity.ok(convertToDTO(account));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username);
        if (username==null){
            throw new UsernameNotFoundException("Account not found");
        }
        return new User(account.getUsername(), account.getPassword(), rolesToAuthorities(account.getRoles()));
    }
    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
