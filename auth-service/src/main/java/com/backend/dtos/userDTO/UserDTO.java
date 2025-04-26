package com.backend.dtos.userDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
@NoArgsConstructor
@Data
public class UserDTO implements Serializable {

    private String fullName;
}