package com.swisscom.tasks.task3.controller;

import com.swisscom.tasks.task3.dto.user.UserDTO;
import com.swisscom.tasks.task3.model.auth.LoginRequest;
import com.swisscom.tasks.task3.model.auth.LoginResponseDTO;
import com.swisscom.tasks.task3.model.auth.User;
import com.swisscom.tasks.task3.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody LoginRequest body){
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        LoginResponseDTO loginResponseDTO = authenticationService.loginUser(loginRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, loginResponseDTO.getJwt())
                .body(loginResponseDTO);
    }
    
}
