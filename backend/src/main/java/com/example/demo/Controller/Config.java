package com.example.demo.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class Config {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/home").permitAll()
                                                .anyRequest().authenticated())
                                .oauth2Login(withDefaults())
                                .formLogin(withDefaults())
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Specify
                                                                                                            // the
                                                                                                            // logout
                                                                                                            // URL
                                                .logoutSuccessUrl("/home") // Redirect after successful logout
                                                .invalidateHttpSession(true) // Invalidate the HTTP session
                                                .clearAuthentication(true) // Clear the authentication
                                                .permitAll() // Allow access to the logout URL for all
                                );
                return http.build();
        }
}