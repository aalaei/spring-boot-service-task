package com.swisscom.tasks.task3.controller;

import com.swisscom.tasks.task3.dto.auth.UserDTO;
import com.swisscom.tasks.task3.dto.auth.LoginRequestDTO;
import com.swisscom.tasks.task3.dto.auth.LoginResponseDTO;
import com.swisscom.tasks.task3.dto.service.ServiceODTODefault;
import com.swisscom.tasks.task3.exception.AuthenticationServiceException;
import com.swisscom.tasks.task3.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
@Tag(name="Authentication", description = "Authentication API")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    /**
     * Registers a user.
     * @param body - {@link LoginRequestDTO} of the user.
     * @return - {@link UserDTO} of new created user.
     */
    @Operation(
            description = "Register a user by username and password",
            summary = "User Registration",
            parameters = {
                    @Parameter(
                            required = true,
                            description = "User credentials",
                            in = ParameterIn.PATH,
                            name = "serviceODTO",
                            schema = @Schema(implementation = LoginRequestDTO.class)
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "OK. User created.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "User Already Exists",
                            responseCode = "409"
                    ),
                    @ApiResponse(
                            description = "USER ROLE NOT FOUND",
                            responseCode = "404"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody LoginRequestDTO body){
        try {
            return ResponseEntity.ok(
                    authenticationService.registerUser(body.getUsername(), body.getPassword())
            );
        }catch (AuthenticationServiceException e){
            if(e.getMessage().startsWith("Role")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
        }
    }

    /**
     * Login a user.
     * @param loginRequestDTO - {@link LoginRequestDTO} of the user. It contains username and password.
     * @return - {@link LoginResponseDTO} of the user. It contains the user and the generated JWT.
     */
    @Operation(
            description = "Login a user by username and password",
            summary = "User Login",
            parameters = {
                    @Parameter(
                            required = true,
                            description = "User credentials",
                            in = ParameterIn.PATH,
                            name = "loginRequestDTO",
                            schema = @Schema(implementation = LoginRequestDTO.class)
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "OK. User logged in.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Invalid Credentials",
                            responseCode = "401"
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        try {
            LoginResponseDTO loginResponseDTO = authenticationService.loginUser(loginRequestDTO);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, loginResponseDTO.getJwt())
                    .body(loginResponseDTO);
        }catch (AuthenticationServiceException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
