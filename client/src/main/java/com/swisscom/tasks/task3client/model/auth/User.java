package com.swisscom.tasks.task3client.model.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * User class. It is used to represent a user in the application for Authentication and Authorization.
 */
@Data
@NoArgsConstructor
public class User {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String title;
    private List<Role> roles;

    public User(String username, String password, List<Role> roles) {
        if(username.equals("me"))
            throw new IllegalStateException("Username cannot be 'me'.");
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.email= username+"@example.com";
    }

    public User(String id, String password) {
        this.id = id;
        this.password = password;
        this.roles = List.of(new Role(Role.RoleType.USER));
    }
}
