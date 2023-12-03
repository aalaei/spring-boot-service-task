package com.swisscom.tasks.task3.controller;

import com.swisscom.tasks.task3.model.LoginRequest;
import com.swisscom.tasks.task3.model.LoginResponseDTO;
import com.swisscom.tasks.task3.model.User;
import com.swisscom.tasks.task3.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public User registerUser(@RequestBody LoginRequest body){
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody LoginRequest body){
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }
}
