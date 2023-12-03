package com.swisscom.tasks.task3.model.auth;

import com.swisscom.tasks.task3.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponseDTO {
    private UserDTO user;
    private String jwt;

}
