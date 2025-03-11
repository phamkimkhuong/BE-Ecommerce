/*
 * @(#) $(NAME).java    1.0     2/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.repositories;


import com.backend.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 26-February-2025 10:52 PM
 */
@RepositoryRestResource(collectionResourceRel = "account", path = "account")
public interface AccountRepository extends JpaRepository<Account, Long> {
    public boolean existsAccountByUsername(String username);
    public boolean existsAccountByEmail(String email);
    public Account findAccountByUsername(String username);
    public Account findAccountByEmail(String email);
}

    