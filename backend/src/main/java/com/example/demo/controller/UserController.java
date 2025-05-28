package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "https://frontend-jh-74d9be1b01e4.herokuapp.com", allowCredentials = "true")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/email/{email}/profile")
    public ResponseEntity<User> updateProfileByEmail(@PathVariable String email, @RequestBody User updatedUser) {
        try {
            User user = userService.updateProfileByEmail(email, updatedUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<User> updateProfile(@PathVariable String id, @RequestBody User updatedUser) {
        try {
            User user = userService.updateProfile(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/moderator")
    public ResponseEntity<User> setModeratorStatus(
            @PathVariable String id,
            @RequestParam boolean isModerator) {
        try {
            User user = userService.setModeratorStatus(id, isModerator);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting user: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal Object principal) {
        String email = null;
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
            email = (String) oauth2User.getAttribute("email");
        }
        if (email == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}
