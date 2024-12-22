package com.security.simple.filter.exception;

import com.security.simple.filter.exception.TokenException;
import org.springframework.http.HttpStatus;

public class InvalidSignatureException extends TokenException {

    public InvalidSignatureException() {
        super(HttpStatus.UNAUTHORIZED, "invalid signature");
    }
}
