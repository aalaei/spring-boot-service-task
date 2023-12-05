package com.swisscom.tasks.task3.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login response DTO. It contains the user and the generated JWT.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponseDTO {
    private UserDTO user;
    private String jwt;

}
