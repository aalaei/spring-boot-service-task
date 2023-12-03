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

/**
 * This class is used to authenticate a user.
 */
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

    /**
     * This method returns a user by username.
     * @param username - The username({@link String}) of the user.
     * @return - {@link User} object.
     */
    public User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () ->  new AuthenticationServiceException("User not found: "+ username)
        );
    }

    /**
     * This method registers a user with USER Role. It saves the user in the database.
     * @param username - username({@link String}) of the user.
     * @param password - password({@link String}) of the user.
     * @return - {@link User} object.
     */
    public UserDTO registerUser(String username, String password){

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority(Role.RoleType.USER.name()).orElseThrow(
                () -> new AssertionError("Role not found: USER")
        );
        return userDTOMapper.apply(userRepository.save(new User(username, encodedPassword, List.of(userRole))));
    }

    /**
     * This method authenticates a user given login credentials.
     * @param loginRequest - {@link LoginRequest} object.
     * @return - {@link LoginResponseDTO} object.
     */
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

    /**
     * This method returns a {@link UserDTO} object given a username.
     * @param self - username({@link String}) of the user requesting the user.
     * @param username - username({@link String}) of the user.
     * @return - {@link UserDTO} object.
     */
    public UserDTO getUserDTO(String self, String username){
        User selfUser = getUser(self);
        if(selfUser.getRoles().stream().anyMatch(role ->
                role.getAuthority().equals(Role.RoleType.ADMIN.name())) || self.equals(username))
            return userDTOMapper.apply(getUser(username));
        throw new AuthenticationServiceException("You are not authorized to view this user");
    }

    /**
     * This method edits a user.
     * @param self - username({@link String}) of the user editing the user.
     * @param username - username({@link String}) of the user to be edited.
     * @param user - new user details({@link User}).
     * @return - {@link UserDTO} object of the updated user.
     */
    public UserDTO editUser(String self, String username, User user){
        User selfUser = getUser(self);
        if(selfUser.getRoles().stream().noneMatch(role ->
                role.getAuthority().equals(Role.RoleType.ADMIN.name())))
            throw new AuthenticationServiceException("You are not authorized to edit this user");
        User userToEdit = getUser(username);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(userToEdit.getId());
        return userDTOMapper.apply(userRepository.save(user));
    }

    /**
     * This method deletes a user.
     * @param self - username({@link String}) of the user deleting the user.
     * @param username - username({@link String}) of the user to be deleted.
     */
    public void deleteUser(String self, String username){
        User selfUser = getUser(self);
        if(selfUser.getRoles().stream().noneMatch(role ->
                role.getAuthority().equals(Role.RoleType.ADMIN.name())))
            throw new AuthenticationServiceException("You are not authorized to edit this user");
        userRepository.delete(getUser(username));
    }
}
