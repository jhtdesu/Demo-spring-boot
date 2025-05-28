package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    public CustomOAuth2UserService() {
        System.out.println("CustomOAuth2UserService initialized!");
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        System.out.println("CustomOAuth2UserService.loadUser called");
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        System.out.println("OAuth2 login: email=" + email + ", name=" + name);

        // Check if user exists
        User user = userRepository.findByEmail(email).orElse(null);

        // If user doesn't exist, create a new one
        if (user == null) {
            System.out.println("Creating new user for email: " + email);
            user = new User();
            user.setEmail(email);
            user.setName(name != null ? name : "No Name");
            user.setModerator(false);
            user.setProfilePicture(null);
            user.setBio("");
            user.setLocation("");
            user.setJoinDate(java.time.LocalDateTime.now().toString());
            user = userRepository.save(user);
            System.out.println("New user created with ID: " + user.getId());
        }

        return oAuth2User;
    }
}