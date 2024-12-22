package com.security.simple.auth;


public class AuthContext {

    private static final ThreadLocal<AuthUser> USER_CONTEXT = new ThreadLocal<>();

    public static void setUser(AuthUser user) {
        USER_CONTEXT.set(user);
    }

    public static AuthUser getUser() {
        return USER_CONTEXT.get();
    }

    public static void clear() {
        USER_CONTEXT.remove();
    }
}
