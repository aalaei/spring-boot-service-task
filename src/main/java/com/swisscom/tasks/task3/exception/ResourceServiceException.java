package com.swisscom.tasks.task3.exception;

/**
 * This class is used to represent an exception in Resource Service.
 */
public class ResourceServiceException extends RuntimeException{
    /**
     * Constructor for ResourceServiceException
     *
     * @param message - Error message for ResourceServiceException
     */
    public ResourceServiceException(String message) {
        super(message);
    }
}
