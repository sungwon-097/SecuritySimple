package com.security.simple.filter.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class TokenException extends RuntimeException {

    private final HttpStatus httpStatus;

    public TokenException(HttpStatus status, String message) {
        super(message);
        this.httpStatus = status;
    }
}
