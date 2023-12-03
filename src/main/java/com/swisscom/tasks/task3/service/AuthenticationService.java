package com.swisscom.tasks.task3.service;

import com.swisscom.tasks.task3.dto.user.UserDTO;
import com.swisscom.tasks.task3.dto.user.UserDTOMapper;
import com.swisscom.tasks.task3.exception.AuthenticationServiceException;
import com.swisscom.tasks.task3.model.auth.LoginRequest;
import com.swisscom.tasks.task3.model.auth.LoginResponseDTO;
import com.swisscom.tasks.task3.model.auth.Role;
import com.swisscom.tasks.task3.model.auth.User;
import com.swisscom.tasks.task3.repository.RoleRepository;
import com.swisscom.tasks.task3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;
    private final UserDTOMapper userDTOMapper;

    public User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () ->  new AuthenticationServiceException("User not found: "+ username)
        );
    }

    public User registerUser(String username, String password){

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").orElseThrow(
                () -> new AssertionError("Role not found: USER")
        );
        return userRepository.save(new User(username, encodedPassword, List.of(userRole)));
    }

    public LoginResponseDTO loginUser(LoginRequest loginRequest){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

//        User user = (User) auth.getPrincipal();
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(
                () -> new AuthenticationServiceException("User not found: "+ loginRequest.getUsername())
        );
        String token = tokenService.generateToken(auth);
        return new LoginResponseDTO(userDTOMapper.apply(user), token);
    }
}
