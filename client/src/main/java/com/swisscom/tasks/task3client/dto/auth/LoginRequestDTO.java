package com.swisscom.tasks.task3client.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Login request DTO.
 */
@Getter
@AllArgsConstructor
public class LoginRequestDTO {
    String username;
    String password;
}
