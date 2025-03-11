/*
 * @(#) $(NAME).java    1.0     2/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.entities;

import jakarta.persistence.*;

import java.util.List;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 26-February-2025 10:41 PM
 */
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "account_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> accounts;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
