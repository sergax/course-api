package com.sergax.courseapi.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
public class InvalidMentorException extends RuntimeException {
    public InvalidMentorException(String message) {
        super(message);
    }
}
