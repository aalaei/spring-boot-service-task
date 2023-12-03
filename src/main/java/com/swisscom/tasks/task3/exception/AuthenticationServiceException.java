package com.swisscom.tasks.task3.exception;

/**
 * This exception is thrown when an error occurs in the authentication service.
 */
public class AuthenticationServiceException extends RuntimeException{
    /**
     * Constructor of AuthenticationServiceException.
     * @param message The message of the exception.
     */
    public AuthenticationServiceException(String message) {
        super(message);
    }
}
