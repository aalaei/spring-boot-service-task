package com.swisscom.tasks.task3client.exception;

import org.springframework.http.ResponseEntity;

public class HttpCallException extends RuntimeException{
    ResponseEntity<?> responseEntity;
    public HttpCallException(String message, ResponseEntity<?> responseEntity) {
        super(message);
        this.responseEntity=responseEntity;
    }
}
