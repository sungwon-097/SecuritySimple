package com.security.simple.filter.exception;

import com.security.simple.filter.exception.TokenException;
import org.springframework.http.HttpStatus;

public class MalformedTokenException extends TokenException {

    public MalformedTokenException() {
        super(HttpStatus.BAD_REQUEST, "malformed token");
    }
}
