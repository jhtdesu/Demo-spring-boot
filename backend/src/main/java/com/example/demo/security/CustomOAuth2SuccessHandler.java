package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.Cookie;

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

        // Set JWT as a cookie (accessible to JS, cross-site)
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(false); // Allow JS access
        jwtCookie.setSecure(true); // Only over HTTPS
        jwtCookie.setMaxAge(24 * 60 * 60); // 1 day
        jwtCookie.setDomain("frontend-jh-74d9be1b01e4.herokuapp.com");
        // Note: SameSite=None is not directly supported by Cookie API in Java, must be
        // set via response header if needed
        response.addCookie(jwtCookie);

        // Redirect to frontend /home
        String redirectUrl = "https://frontend-jh-74d9be1b01e4.herokuapp.com/home";
        response.sendRedirect(redirectUrl);
    }
}