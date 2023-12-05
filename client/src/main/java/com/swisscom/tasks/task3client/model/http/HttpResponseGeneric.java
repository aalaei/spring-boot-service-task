package com.swisscom.tasks.task3client.model.http;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

@Data
public class HttpResponseGeneric<T> implements Serializable {
    protected String timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String reason;
    protected String message;
    protected String developerMessage;
    protected Map<String, T> data;
}
