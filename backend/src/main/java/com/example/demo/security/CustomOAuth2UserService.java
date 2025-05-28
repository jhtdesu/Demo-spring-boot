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

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    logger.info("Creating new user for email: {}", email);
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name != null ? name : "No Name");
                    newUser.setProvider("google");
                    newUser.setProviderId(providerId);
                    newUser.setProfilePicture(picture);
                    newUser.setModerator(false);
                    newUser.setBio("");
                    newUser.setLocation("");
                    newUser.setJoinDate(java.time.LocalDateTime.now().toString());
                    return userRepository.save(newUser);
                });

        // Always update the user with the latest OAuth2 data
        user.setName(name != null ? name : user.getName());
        user.setProvider("google");
        user.setProviderId(providerId);
        if (picture != null) {
            user.setProfilePicture(picture);
        }
        userRepository.save(user);

        return oAuth2User;
    }
}