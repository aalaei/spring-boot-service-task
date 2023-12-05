package com.swisscom.tasks.task3.exception;

/**
 * This exception is thrown when an error occurs in the encryption service.
 */
public class EncryptionException extends RuntimeException{
    public EncryptionException(String message) {
        super(message);
    }
}
