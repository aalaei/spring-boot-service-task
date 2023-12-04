package com.swisscom.tasks.task3client.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * User DTO.
 */
@Data
@AllArgsConstructor
public class UserEditDTO {
    private String password;
    private String firstName;
    private String lastName;
    private String title;
    private List<String> roles;

}
