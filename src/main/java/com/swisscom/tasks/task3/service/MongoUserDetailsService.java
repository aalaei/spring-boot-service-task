package com.swisscom.tasks.task3.service;

import com.swisscom.tasks.task3.exception.UserServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * MongoUserDetailsService it implements data persistence for the UserDetailsService interface
 */
@Service
@RequiredArgsConstructor
public class MongoUserDetailsService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userService.getUser(username);
        }catch (UserServiceException e){
            throw new UsernameNotFoundException("User not found: "+ username);
        }
    }
}
