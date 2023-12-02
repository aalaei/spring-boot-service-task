package com.swisscom.tasks.task3.exception;

/**
 * This class is used to represent an exception in ServiceObjectService.
 */
public class ServiceOServiceException extends RuntimeException {
    /**
     * Constructor for ServiceOServiceException
     *
     * @param message - Error message for ServiceOServiceException
     */
    public ServiceOServiceException(String message) {
        super(message);
    }
}
