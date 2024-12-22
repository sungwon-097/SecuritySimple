package com.security.simple.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class AuthUserImpl implements AuthUser {

    private final String username;
    private final String roles;
    private final List<String> claims;

    public AuthUserImpl(DecodedJWT decoded) {
        this.username = decoded.getSubject();
        this.roles = String.valueOf(decoded.getClaim("roles"));
        this.claims = Arrays.stream(String.valueOf(decoded.getClaim("claims")).split(",")).toList();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getRoles() {
        return this.roles;
    }

    @Override
    public List<String> getClaims() {
        return this.claims;
    }
}
