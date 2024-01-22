package com.example.newproject.services;


public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    // You can also add other constructors, methods, or fields as needed
}
