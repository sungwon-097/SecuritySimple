package com.security.simple.utils.algorithm;

public enum JwtAlgorithm {
    HS256,
    HS384,
    HS512,
    RS256,
    RS384,
    RS512,
    PS256,
    PS384,
    PS512;

    public JwtAlgorithm fromString(String value) {
        try {
            return JwtAlgorithm.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported JWT Algorithm: " + value);
        }
    }
}

