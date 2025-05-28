package com.example.demo.config;

import com.example.demo.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import com.example.demo.security.CustomOAuth2UserService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import jakarta.servlet.http.HttpServletResponse;
import com.example.demo.security.JwtUtil;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.Collections;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

        @Autowired
        private JwtFilter jwtFilter;

        @Autowired
        private CustomOAuth2UserService customOAuth2UserService;

        @Autowired
        private JwtUtil jwtUtil;

        public SecurityConfig() {
                logger.info("SecurityConfig initialized!");
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of(
                                "https://frontend-jh-74d9be1b01e4.herokuapp.com",
                                "https://backend-jh-cff06dd28ef7.herokuapp.com"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setExposedHeaders(List.of("Set-Cookie"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public AuthenticationEntryPoint restAuthenticationEntryPoint() {
                return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
        }

        // Custom entry point to restrict OAuth2 login initiation to frontend
        @Bean
        public AuthenticationEntryPoint restrictedOAuth2EntryPoint() {
                return (request, response, authException) -> {
                        String referer = request.getHeader("Referer");
                        if (referer == null || !referer.startsWith("https://frontend-jh-74d9be1b01e4.herokuapp.com")) {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                                "Login must be initiated from the frontend");
                        } else {
                                // Fallback to default behavior (redirect to login page)
                                new LoginUrlAuthenticationEntryPoint("/oauth2/authorization/google").commence(request,
                                                response, authException);
                        }
                };
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                logger.info("Configuring SecurityFilterChain");
                http
                                .cors(Customizer.withDefaults())
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/",
                                                                "/index.html",
                                                                "/static/**",
                                                                "/*.js", "/*.css", "/*.ico", "/*.png", "/*.svg",
                                                                "/home",
                                                                "/login",
                                                                "/register",
                                                                "/error",
                                                                "/api/auth/login",
                                                                "/api/auth/register",
                                                                "/oauth2/**",
                                                                "/login/oauth2/**",
                                                                "/api/auth/oauth2/success",
                                                                "/api/auth/google/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .oauth2Login(oauth2 -> oauth2
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService))
                                                .successHandler(myAuthenticationSuccessHandler()))
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout"))
                                                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                                                .deleteCookies("jwt")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .permitAll())
                                .exceptionHandling(eh -> eh
                                                .authenticationEntryPoint(restAuthenticationEntryPoint())
                                                .defaultAuthenticationEntryPointFor(restrictedOAuth2EntryPoint(),
                                                                new AntPathRequestMatcher(
                                                                                "/oauth2/authorization/google")));
                http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        @Bean
        public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
                return (request, response, authentication) -> {
                        try {
                                String email = null;
                                if (authentication
                                                .getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
                                        email = oauth2User.getAttribute("email");
                                        logger.info("OAuth2 login successful for email: {}", email);
                                }

                                if (email != null) {
                                        String jwtToken = jwtUtil.generateToken(email);
                                        ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                                                        .httpOnly(true)
                                                        .secure(true)
                                                        .path("/")
                                                        .maxAge(24 * 60 * 60)
                                                        .sameSite("None")
                                                        .build();
                                        response.addHeader("Set-Cookie", cookie.toString());
                                        logger.info("JWT cookie set successfully for {}", email);

                                        // Set the authentication in the SecurityContext
                                        var userDetails = new org.springframework.security.core.userdetails.User(
                                                        email,
                                                        "",
                                                        Collections.singletonList(
                                                                        new SimpleGrantedAuthority("ROLE_USER")));
                                        var authToken = new UsernamePasswordAuthenticationToken(
                                                        userDetails,
                                                        null,
                                                        userDetails.getAuthorities());
                                        SecurityContextHolder.getContext().setAuthentication(authToken);

                                        response.sendRedirect("https://frontend-jh-74d9be1b01e4.herokuapp.com/");
                                } else {
                                        logger.error("Email not found in OAuth2 user attributes");
                                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                        response.getWriter().write("{\"error\": \"Email not found\"}");
                                }
                        } catch (Exception e) {
                                logger.error("Error in OAuth2 success handler", e);
                                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                response.getWriter().write("{\"error\": \"Authentication failed\"}");
                        }
                };
        }
}