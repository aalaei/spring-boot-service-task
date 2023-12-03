package com.swisscom.tasks.task3.exception;

public class AuthenticationServiceException extends RuntimeException{
    public AuthenticationServiceException(String message) {
        super(message);
    }
}
