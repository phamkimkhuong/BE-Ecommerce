/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.userservice.repositories;


import com.backend.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 13-February-2025 8:05 PM
 */
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByAccountId(Long accountId);

    User findByAccountId(Long accountId);
}

    