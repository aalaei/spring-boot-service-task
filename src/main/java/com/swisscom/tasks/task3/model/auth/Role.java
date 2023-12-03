package com.swisscom.tasks.task3.model.auth;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

/**
 * Role class. It is used to represent a role in the application.
 */
@Data
@Document
@NoArgsConstructor
public class Role implements GrantedAuthority {
    public enum RoleType{
        USER,
        ADMIN,
        SUPER_USER,
    }
    @Id
    private String id;
    @Indexed(unique = true)
    private String authority;

    public Role(RoleType roleType) {
        this.authority = roleType.name();
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
