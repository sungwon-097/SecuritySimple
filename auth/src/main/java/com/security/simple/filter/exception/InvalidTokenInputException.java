package com.security.simple.filter.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenInputException extends TokenException {

    public InvalidTokenInputException() {
        super(HttpStatus.BAD_REQUEST, "invalid token input");
    }
}
