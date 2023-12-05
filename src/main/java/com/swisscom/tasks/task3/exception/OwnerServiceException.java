package com.swisscom.tasks.task3.exception;

/**
 * This class is used to represent an exception in Owner Service.
 */
public class OwnerServiceException extends RuntimeException {
    /**
     * Constructor for OwnerServiceException
     *
     * @param message - Error message for OwnerServiceException
     */
    public OwnerServiceException(String message) {
        super(message);
    }
}
