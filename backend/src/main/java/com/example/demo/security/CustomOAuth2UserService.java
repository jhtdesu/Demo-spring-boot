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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private UserRepository userRepository;

    public CustomOAuth2UserService() {
        System.out.println("CustomOAuth2UserService initialized!");
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        logger.info("Processing OAuth2 login request");
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub");
        String picture = oAuth2User.getAttribute("picture");

        logger.info("OAuth2 user details - email: {}, name: {}, providerId: {}", email, name, providerId);

        // Check if user exists
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // Create new user if not exists
            user = new User();
            user.setEmail(email);
            user.setName(name != null ? name : "No Name");
            user.setProvider("google");
            user.setProviderId(providerId);
            user.setProfilePicture(picture);
            user.setJoinDate(java.time.LocalDateTime.now().toString());
            user.setBio("");
            user.setLocation("");
            user.setModerator(false);

            // Save the new user
            user = userRepository.save(user);
            logger.info("New user created via OAuth2: {}", user.getEmail());
        } else {
            // Update existing user's OAuth2 information
            user.setName(name != null ? name : user.getName());
            user.setProvider("google");
            user.setProviderId(providerId);
            user.setProfilePicture(picture);

            // Save the updated user
            user = userRepository.save(user);
            logger.info("Existing user updated via OAuth2: {}", user.getEmail());
        }

        // Create a new OAuth2User with the user's authorities
        return new org.springframework.security.oauth2.core.user.DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "email");
    }
}