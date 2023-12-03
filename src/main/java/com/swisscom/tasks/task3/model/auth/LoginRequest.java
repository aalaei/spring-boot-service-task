package com.swisscom.tasks.task3.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Login request.
 */
@Getter
@AllArgsConstructor
public class LoginRequest {
    String username;
    String password;
}
