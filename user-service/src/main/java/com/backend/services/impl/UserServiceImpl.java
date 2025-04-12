/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.services.impl;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 13-February-2025 8:15 PM
 */

import com.backend.commonservice.model.AppException;
import com.backend.commonservice.model.ErrorMessage;
import com.backend.dtos.CreateUserRequest;
import com.backend.dtos.UserDTO;
import com.backend.entities.User;
import com.backend.repositories.UserRepository;
import com.backend.services.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        return user;
    }


    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorMessage.RESOURCE_NOT_FOUND));

        return this.convertToDTO(user);
    }

    @Transactional
    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = this.convertToEntity(userDTO);
        user = userRepository.save(user);
        return this.convertToDTO(user);
    }

    @Override
    public boolean delete(Long id) {
        this.findById(id);
        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    @Override
    public CreateUserRequest createUserRequest(CreateUserRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());

        user.setFullName(null);
        user.setPhoneNumber(null);
        user.setAddress(null);
        user.setDateOfBirth(null);
        user.setGender(false); // mặc định

        userRepository.save(user);
        return request;
    }
}
