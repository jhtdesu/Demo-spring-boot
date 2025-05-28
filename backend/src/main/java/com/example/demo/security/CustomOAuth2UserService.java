package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private UserRepository userRepository;

    public CustomOAuth2UserService() {
        logger.info("CustomOAuth2UserService initialized!");
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        logger.info("TEST: CustomOAuth2UserService.loadUser called!");
        throw new RuntimeException("TEST: CustomOAuth2UserService.loadUser called!");
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        logger.info("OAuth2 login attempt - email: {}, name: {}", email, name);

        try {
            // Check if user exists
            User user = userRepository.findByEmail(email).orElse(null);
            logger.info("Existing user check - found: {}", user != null);

            // If user doesn't exist, create a new one
            if (user == null) {
                logger.info("Creating new user for email: {}", email);
                user = new User();
                user.setEmail(email);
                user.setName(name != null ? name : "No Name");
                user.setModerator(false);
                user.setProfilePicture(null);
                user.setBio("");
                user.setLocation("");
                user.setJoinDate(java.time.LocalDateTime.now().toString());

                try {
                    user = userRepository.save(user);
                    logger.info("New user created successfully with ID: {}", user.getId());
                } catch (Exception e) {
                    logger.error("Error saving new user to database: {}", e.getMessage(), e);
                    throw e;
                }
            } else {
                logger.info("Existing user found with ID: {}", user.getId());
            }

            return oAuth2User;
        } catch (Exception e) {
            logger.error("Error in OAuth2 user processing: {}", e.getMessage(), e);
            throw e;
        }
    }
}