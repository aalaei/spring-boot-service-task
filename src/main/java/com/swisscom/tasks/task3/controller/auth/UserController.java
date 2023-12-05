package com.swisscom.tasks.task3.controller.auth;

import com.swisscom.tasks.task3.dto.auth.UserDTO;
import com.swisscom.tasks.task3.dto.auth.UserEditDTO;
import com.swisscom.tasks.task3.exception.AuthenticationServiceException;
import com.swisscom.tasks.task3.exception.UserServiceException;
import com.swisscom.tasks.task3.model.auth.User;
import com.swisscom.tasks.task3.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * User controller.
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
@RequestMapping("/api/v1/users")
public class UserController {

    private final AuthenticationService authenticationService;

    /**
     * Registers a user.
     *
     * @param principal - {@link Principal} of the user.
     * @param username  - {@link String} of the user. if username is not specified then all users are returned.
     * @return - {@link ResponseEntity} containing the new created user of type {@link User}.
     */
    @Operation(
            description = "Get a user by username. if The username is not specified then all users are returned.",
            summary = "Get a user by username",
            parameters = {
                    @Parameter(
                            required = false,
                            description = "Username",
                            in = ParameterIn.PATH,
                            name = "username",
                            schema = @Schema(implementation = String.class)
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "OK. User found.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    )
            }
    )
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping
    public ResponseEntity<?> getUser(Principal principal,
                                     @RequestParam(value = "username", required = false) String username) {
        try {
            if (username == null || username.isEmpty())
                return ResponseEntity.ok(authenticationService.getAllUserDTOs(principal.getName()));
            return ResponseEntity.ok(authenticationService.getUserDTO(principal.getName(), username));
        } catch (AuthenticationServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    /**
     * Update a user.
     *
     * @param principal - {@link Principal} of the user.
     * @param username  - {@link String} of the user.
     * @param user      - {@link UserEditDTO} of the user.
     * @return {@link ResponseEntity} of {@link UserDTO}
     */
    @Operation(
            description = "Update a user by username",
            summary = "Update a user by username",
            parameters = {
                    @Parameter(
                            required = true,
                            description = "Username",
                            in = ParameterIn.PATH,
                            name = "username",
                            schema = @Schema(implementation = String.class)
                    ),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserEditDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "OK. User updated.",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    )
            }
    )
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @PutMapping
    public ResponseEntity<?> updateUser(Principal principal, @RequestParam("username") String username,
                                        @RequestBody UserEditDTO user) {
        try {
            return ResponseEntity.ok(authenticationService.editUser(principal.getName(), username, user));
        } catch (UserServiceException e) {
            HttpStatus httpStatus = e.getMessage().contains("not found") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            ;
            return ResponseEntity.status(httpStatus).body(e.getMessage());
        } catch (AuthenticationServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @DeleteMapping()
    public ResponseEntity<?> deleteUsers(Principal principal, @RequestParam(value = "username") String username) {
        try {
            authenticationService.deleteUser(principal.getName(), username);
            return ResponseEntity.ok().body("User '" + username + "' is deleted");
        } catch (UserServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AuthenticationServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
