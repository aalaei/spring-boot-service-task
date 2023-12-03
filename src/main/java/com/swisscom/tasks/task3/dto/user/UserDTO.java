package com.swisscom.tasks.task3.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * User DTO.
 */
@Data
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String title;
    private List<String> roles;

}
