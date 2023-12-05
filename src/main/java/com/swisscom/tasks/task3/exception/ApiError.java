package com.swisscom.tasks.task3.exception;

import java.time.LocalDateTime;

/**
 * This class is used to represent an error in the application api.
 */
public record ApiError(
        String path,
        String message,
        int statusCode,
        LocalDateTime localDateTime
) {

}
