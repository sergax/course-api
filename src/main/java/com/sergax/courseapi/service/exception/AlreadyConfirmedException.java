package com.sergax.courseapi.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
public class AlreadyConfirmedException extends RuntimeException {
    public AlreadyConfirmedException(String format) {
        super(format);
    }
}
