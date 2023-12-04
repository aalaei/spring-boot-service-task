package com.swisscom.tasks.task3.service;

import com.swisscom.tasks.task3.dto.auth.*;
import com.swisscom.tasks.task3.exception.AuthenticationServiceException;
import com.swisscom.tasks.task3.model.auth.Role;
import com.swisscom.tasks.task3.model.auth.User;
import com.swisscom.tasks.task3.repository.RoleRepository;
import com.swisscom.tasks.task3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * This class is used to authenticate a user.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    /**
     * This method returns a user by username.
     * @param username - The username({@link String}) of the user.
     * @return - {@link User} object.
     */
    public UserDTO getUserDTO(String username){
        return userService.getUserDTO(username);
    }

    /**
     * This method returns all users.
     * @param self - username({@link String}) of the user requesting the users.
     * @return - {@link List} of {@link User} objects.
     */
    public List<UserDTO> getAllUserDTOs(String self){
        UserDTO selfUser = userService.getUserDTO(self);
        List<UserDTO> users = userService.getAllUserDTOs();
        if (selfUser.getRoles().stream().anyMatch(role -> role.equals(Role.RoleType.ADMIN.name())))
            return users;
        return users.stream().filter(user -> user.getUsername().equals(self)).toList();
    }

    /**
     * This method registers a user with USER Role. It saves the user in the database.
     * @param username - username({@link String}) of the user.
     * @param password - password({@link String}) of the user.
     * @return - {@link User} object.
     */
    public UserDTO registerUser(String username, String password){
       return userService.registerUser(username, password);
    }

    /**
     * This method authenticates a user given login credentials.
     * @param loginRequestDTO - {@link LoginRequestDTO} object.
     * @return - {@link LoginResponseDTO} object.
     */
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );
        UserDTO userDTO = userService.getUserDTO(loginRequestDTO.getUsername());
        String token = tokenService.generateToken(auth);
        return new LoginResponseDTO(userDTO, token);
    }

    /**
     * This method returns a {@link UserDTO} object given a username.
     * @param self - username({@link String}) of the user requesting the user.
     * @param username - username({@link String}) of the user.
     * @return - {@link UserDTO} object.
     */
    public UserDTO getUserDTO(String self, String username){
        UserDTO selfUser = userService.getUserDTO(self);
        if(Objects.equals(username, "me"))
            return selfUser;
        if(selfUser.getRoles().stream().anyMatch(role ->
                role.equals(Role.RoleType.ADMIN.name())) || self.equals(username))
            return userService.getUserDTO(username);
        throw new AuthenticationServiceException("You are not authorized to view this user");
    }

    /**
     * This method edits a user.
     * @param self - username({@link String}) of the user editing the user.
     * @param username - username({@link String}) of the user to be edited.
     * @param user - new user details({@link User}).
     * @return - {@link UserDTO} object of the updated user.
     */
    public UserDTO editUser(String self, String username, UserEditDTO user){
        UserDTO selfUser = userService.getUserDTO(self);
        if(selfUser.getRoles().stream().noneMatch(role ->
                role.equals(Role.RoleType.ADMIN.name())) && !self.equals(username))
            throw new AuthenticationServiceException("You are not authorized to edit this user");
        return userService.updateUser(username, user);
    }

    /**
     * This method deletes a user.
     * @param self - username({@link String}) of the user deleting the user.
     * @param username - username({@link String}) of the user to be deleted.
     */
    public void deleteUser(String self, String username){
        UserDTO selfUser = userService.getUserDTO(self);
        if(selfUser.getRoles().stream().noneMatch(role ->
                role.equals(Role.RoleType.ADMIN.name())) && !self.equals(username))
            throw new AuthenticationServiceException("You are not authorized to delete this user");
        userService.deleteByUsername(username);
    }
}
