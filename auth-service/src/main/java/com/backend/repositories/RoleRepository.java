/*
 * @(#) $(NAME).java    1.0     3/11/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.repositories;


import com.backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 11-March-2025 8:51 PM
 */
@RepositoryRestResource(collectionResourceRel = "role", path = "role")
public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findRoleByName(String name);
}

    