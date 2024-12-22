package com.security.simple.filter.exception;

import com.security.simple.filter.exception.TokenException;
import org.springframework.http.HttpStatus;

public class ExpiredException extends TokenException {

    public ExpiredException() {
        super(HttpStatus.UNAUTHORIZED, "token has expired");
    }
}
