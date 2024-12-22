package com.security.project.user;

public record Response(){
    public record LoginResponse(String access, String refresh){}
}
