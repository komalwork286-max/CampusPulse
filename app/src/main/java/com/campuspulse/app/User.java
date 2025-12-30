package com.campuspulse.app;

public class User {
    public String userId;
    public String name;
    public String email;
    public String role;

    public User() {} // Required

    public User(String userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
