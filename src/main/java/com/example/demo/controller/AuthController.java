package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.LoginResponse;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://frontend-jh-74d9be1b01e4.herokuapp.com", allowCredentials = "true")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()));

            // Always get fresh user data
            User user = userService.getUserByEmail(loginRequest.getEmail());
            if (user == null) {
                logger.error("User not found after successful authentication: {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found");
            }

            logger.info("User logged in successfully: {}", user.getEmail());
            return ResponseEntity.ok()
                    .body(new LoginResponse(user.getId(), user.getName(), user.getEmail()));
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<?> oauth2Success(@AuthenticationPrincipal OAuth2User oauth2User) {
        try {
            String email = oauth2User.getAttribute("email");
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok()
                    .body(new LoginResponse(user.getId(), user.getName(), user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth2 authentication failed");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("Logged out successfully");
    }
}