package com.sergax.courseapi.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "This email already exists")
public class NotUniqueDataException extends RuntimeException {
    public NotUniqueDataException(String message) {
        super(message);
    }
}
