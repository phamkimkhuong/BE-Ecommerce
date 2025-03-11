/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.services;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 13-February-2025 8:12 PM
 */

import com.backend.dtos.UserDTO;

import java.util.List;

public interface UserService {
    public UserDTO findById(Long id);

    public List<UserDTO> findAll();

    public UserDTO save(UserDTO nguoiDung);

    public boolean delete(Long id);
}
