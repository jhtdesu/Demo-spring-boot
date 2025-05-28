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

        // Always create or update user with latest OAuth2 data
        User user = userRepository.findByEmail(email)
                .orElse(new User()); // Create new user if not exists

        // Update all user fields with latest OAuth2 data
        user.setEmail(email);
        user.setName(name != null ? name : "No Name");
        user.setProvider("google");
        user.setProviderId(providerId);
        user.setProfilePicture(picture);
        if (user.getJoinDate() == null) {
            user.setJoinDate(java.time.LocalDateTime.now().toString());
        }
        if (user.getBio() == null) {
            user.setBio("");
        }
        if (user.getLocation() == null) {
            user.setLocation("");
        }
        user.setModerator(false);

        // Save the updated user
        User savedUser = userRepository.save(user);
        logger.info("User saved/updated successfully: {}", savedUser.getEmail());

        return oAuth2User;
    }
}