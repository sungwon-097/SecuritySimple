package com.security.simple.filter.exception;

import org.springframework.http.HttpStatus;

public class UnknownTokenException extends TokenException {

    public UnknownTokenException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "unknown exception");
    }
}
