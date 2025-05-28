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

        // Create user if not exists
        userRepository.findByEmail(email).orElseGet(() -> {
            System.out.println("Creating new user for email: " + email);
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name != null ? name : "No Name");
            newUser.setModerator(false);
            newUser.setProfilePicture(null);
            newUser.setBio("");
            newUser.setLocation("");
            newUser.setJoinDate(java.time.LocalDateTime.now().toString());
            return userRepository.save(newUser);
        });

        return oAuth2User;
    }
}