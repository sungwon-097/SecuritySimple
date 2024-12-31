package com.security.project.user;

public record Request(){
    public record LoginRequest(String username, String password){}
    public record RegisterRequest(String username, String password){}
}
