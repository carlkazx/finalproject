package com.example.newproject.response;


public class LoginResponse {
    private String token;

    public String getRole() {
        return role;
    }

    public LoginResponse setRole(String role) {
        this.role = role;
        return this;
    }

    private String role;

    private long expiresIn;

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", role='" + role + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }

}