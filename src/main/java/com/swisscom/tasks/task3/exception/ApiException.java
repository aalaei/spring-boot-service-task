package com.swisscom.tasks.task3.exception;

public class ApiException extends RuntimeException{
    public ApiException(String message){
        super(message);
    }
}
