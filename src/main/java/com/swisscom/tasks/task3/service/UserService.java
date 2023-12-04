package com.swisscom.tasks.task3.service;

import com.swisscom.tasks.task3.dto.auth.UserDTO;
import com.swisscom.tasks.task3.dto.auth.UserDTOMapper;
import com.swisscom.tasks.task3.dto.auth.UserEditDTO;
import com.swisscom.tasks.task3.exception.UserServiceException;
import com.swisscom.tasks.task3.model.auth.Role;
import com.swisscom.tasks.task3.model.auth.User;
import com.swisscom.tasks.task3.repository.RoleRepository;
import com.swisscom.tasks.task3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * This method returns a user by username.
     * @param username - The username({@link String}) of the user.
     * @return - {@link User} object.
     */
    Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    /**
     * This method returns all users.
     * @return - {@link List} of {@link User} objects.
     */
    public List<User> findAll() {
        return userRepository.findAll();
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
                () -> new UserServiceException("Role not found: USER")
        );
        if(userRepository.findByUsername(username).isPresent())
            throw new UserServiceException("User already exists: "+ username);
        return userDTOMapper.apply(userRepository.save(new User(username, encodedPassword, List.of(userRole))));
    }

    /**
     * This method updates a user.
     * @param username - username({@link String}) of the user.
     * @param user - {@link UserEditDTO} object.
     * @return - {@link User} object.
     */
    public UserDTO updateUser(String username, UserEditDTO user) {

        User userToEdit = getUser(username);
        userToEdit.setFirstName(user.getFirstName());
        userToEdit.setLastName(user.getLastName());
        userToEdit.setTitle(user.getTitle());
        userToEdit.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRoles()==null)
            throw  new UserServiceException("Roles cannot be null");
        userToEdit.setRoles(
                user.getRoles().stream().map(role ->
                        roleRepository.findByAuthority(role).orElseThrow(
                                () -> new UserServiceException("Role not found: "+ role)
                        )
                ).collect(Collectors.toList())
        );
        return userDTOMapper.apply(userRepository.save(userToEdit));
    }

    /**
     * This method deletes a user by id.
     * @param id - id({@link String}) of the user.
     */
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    /**
     * This method deletes a user by username.
     * @param username - username({@link String}) of the user.
     */
    public void deleteByUsername(String username) {
        this.deleteById(getUser(username).getId());
    }

    /**
     * This method returns a {@link User} object given a username.
     * @param username - username({@link String}) of the user.
     * @return - {@link User} object.
     */
    private User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () ->  new UserServiceException("User not found: "+ username)
        );
    }

    /**
     * This method returns a {@link UserDTO} object given a username.
     * @param username - username({@link String}) of the user.
     * @return - {@link UserDTO} object.
     */
    public UserDTO getUserDTO(String username){
        return userDTOMapper.apply(getUser(username));
    }

    /**
     * This method returns all {@link UserDTO} objects.
     * @return - {@link List} of {@link UserDTO} objects.
     */
    public List<UserDTO> getAllUserDTOs(){
        return userRepository.findAll().stream().map(userDTOMapper).toList();
    }
}
