package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // Get user email from authentication
        String email = ((org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal())
                .getAttribute("email");
        String token = jwtUtil.generateToken(email);

        // Redirect to frontend with JWT as a query parameter
        String redirectUrl = "https://frontend-jh-74d9be1b01e4.herokuapp.com/oauth2/redirect?token=" + token;
        response.sendRedirect(redirectUrl);
    }
}