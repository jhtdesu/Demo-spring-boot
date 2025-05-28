package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.security.JwtUtil;
import com.example.demo.model.LoginResponse;
import org.springframework.http.ResponseCookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://frontend-jh-74d9be1b01e4.herokuapp.com", allowCredentials = "true")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()));

            String token = jwtUtil.generateToken(loginRequest.getEmail());

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("None")
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.getUserByEmail(user.getEmail()) != null) {
            logger.warn("Registration failed - User already exists: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        userService.register(user);
        logger.info("New user registered: {}", user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("Register successful");
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<?> oauth2Success(@AuthenticationPrincipal OAuth2User oauth2User,
            HttpServletResponse response) {
        try {
            String email = oauth2User.getAttribute("email");
            if (email == null) {
                logger.error("No email found in OAuth2 user attributes");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("OAuth2 authentication failed - no email found");
            }

            User user = userService.getUserByEmail(email);
            if (user == null) {
                logger.error("User not found after OAuth2 authentication: {}", email);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found");
            }

            String token = jwtUtil.generateToken(email);

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .sameSite("None")
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());

            logger.info("OAuth2 user logged in successfully: {}", user.getEmail());
            return ResponseEntity.ok()
                    .body(new LoginResponse(user.getId(), user.getName(), user.getEmail()));
        } catch (Exception e) {
            logger.error("OAuth2 authentication failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth2 authentication failed");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok().body("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal Object principal) {
        String email = null;
        if (principal == null) {
            logger.warn("No principal found in request");
            return ResponseEntity.status(401).build();
        }

        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof OAuth2User oauth2User) {
            email = (String) oauth2User.getAttribute("email");
        }

        if (email == null) {
            logger.warn("No email found in principal");
            return ResponseEntity.status(401).build();
        }

        // Always get fresh user data
        User user = userService.getUserByEmail(email);
        if (user == null) {
            logger.error("User not found for email: {}", email);
            return ResponseEntity.status(404).build();
        }

        logger.info("Retrieved user data for: {}", email);
        return ResponseEntity.ok(user);
    }
}
