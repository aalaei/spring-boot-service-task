package com.swisscom.tasks.task3.exception;

/**
 * This class is used to represent an exception in the application api.
 */
public class ApiException extends RuntimeException{
    /**
     * Constructor for ApiException
     * @param message - Error message for ApiException
     */
    public ApiException(String message){
        super(message);
    }
}
