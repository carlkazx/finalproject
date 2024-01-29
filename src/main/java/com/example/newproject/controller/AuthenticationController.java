package com.example.newproject.controller;

import com.example.newproject.entity.Role;
import com.example.newproject.entity.User;
import com.example.newproject.dtos.LoginUserDto;
import com.example.newproject.dtos.RegisterUserDto;
import com.example.newproject.response.LoginResponse;
import com.example.newproject.services.AuthenticationService;
import com.example.newproject.services.JwtService;
import io.jsonwebtoken.Claims;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }


    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        logger.info("Received signup data: {}", registerUserDto);

        try {
            User registeredUser = authenticationService.signup(registerUserDto);
            logger.info("User registered successfully: {}", registeredUser.getUsername());
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            logger.error("Signup failed for user: {}, error: {}", registerUserDto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            Claims claims = jwtService.decodeToken(jwtToken);
            logger.info("Decoded JWT: {}", claims);

            // Log the generated token
            logger.info("Generated Token: " + jwtToken);

            // Convert the RoleEnum to String using the getter and name() method
            String roleName = authenticatedUser.getRole().getName().name(); // Use the enum's name() method
            // Inside your authenticate method
            logger.info("User role is: {}", roleName); // This should log the user's role
            LoginResponse loginResponse = new LoginResponse()
                    .setToken(jwtToken)
                    .setExpiresIn(jwtService.getExpirationTime())
                    .setRole(roleName); // Set the role name as a string

            // Log successful login
            logger.info("User '{}' with role '{}' successfully logged in",
                    authenticatedUser.getUsername(), roleName);

            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            // Log failed login attempt
            logger.warn("Failed login attempt for user '{}': {}", loginUserDto.getEmail(), e.getMessage());

            // Handle authentication failure and return an appropriate response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}