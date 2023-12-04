package com.swisscom.tasks.task3.exception;

/**
 * UserServiceException is a custom exception class for the UserService.
 */
public class UserServiceException extends RuntimeException{
    public UserServiceException(String message) {
        super(message);
    }
}
