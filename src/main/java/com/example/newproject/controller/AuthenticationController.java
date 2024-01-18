package com.example.newproject.controller;

import com.example.newproject.entity.User;
import com.example.newproject.dtos.LoginUserDto;
import com.example.newproject.dtos.RegisterUserDto;
import com.example.newproject.response.LoginResponse;
import com.example.newproject.services.AuthenticationService;
import com.example.newproject.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping("/auth")
@RestController
@CrossOrigin
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            // Log the generated token
            logger.info("Generated Token: " + jwtToken);

            LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

            // Log successful login
            logger.info("User '{}' successfully logged in", authenticatedUser.getRole());

            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            // Log failed login attempt
            logger.warn("Failed login attempt for user '{}': {}", loginUserDto.getEmail(), e.getMessage());

            // Handle authentication failure and return an appropriate response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}