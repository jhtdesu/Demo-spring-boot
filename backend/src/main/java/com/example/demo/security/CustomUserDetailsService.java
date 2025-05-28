package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Loading user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found for email: {}", email);
                    return new UsernameNotFoundException("User not found");
                });

        logger.debug("Found user: {}, provider: {}", user.getEmail(), user.getProvider());

        // For OAuth2 users, we don't need to check the password
        if ("google".equals(user.getProvider())) {
            logger.debug("Creating UserDetails for OAuth2 user");
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    "", // Empty password for OAuth2 users
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }

        // For form login users, we need the password
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            logger.error("Password is null or empty for form login user: {}", email);
            throw new UsernameNotFoundException("Invalid user credentials");
        }

        logger.debug("Creating UserDetails for form login user");
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
