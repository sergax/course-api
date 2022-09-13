package com.sergax.courseapi.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class InvalidConfirmationCodeException extends RuntimeException {
    public InvalidConfirmationCodeException(String invalid_code) {
        super(invalid_code);
    }
}
