package com.security.simple.auth;

import java.util.List;

public interface AuthUser {

    String getUsername();

    String getRoles();

    List<String> getClaims();
}
