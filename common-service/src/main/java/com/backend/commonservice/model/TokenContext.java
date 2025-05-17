package com.backend.commonservice.model;

public class TokenContext {
    private static final ThreadLocal<String> TOKEN_HOLDER = new ThreadLocal<>();

    public static void setToken(String token) {
        TOKEN_HOLDER.set(token);
        System.out.println("Token set by Thread ID: " + Thread.currentThread().getName());
    }

    public static String getToken() {
        System.out.println("Token accessed by Thread ID: " + Thread.currentThread().getName());
        return TOKEN_HOLDER.get();
    }

    public static void clear() {
        TOKEN_HOLDER.remove();
    }
}
