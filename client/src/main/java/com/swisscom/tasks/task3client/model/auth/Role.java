package com.swisscom.tasks.task3client.model.auth;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role class. It is used to represent a role in the application.
 */
@Data
@NoArgsConstructor
public class Role {
    public enum RoleType {
        USER,
        ADMIN,
        SUPER_USER,
    }

    private String id;
    private String authority;

    public Role(RoleType roleType) {
        this.authority = roleType.name();
    }

}
