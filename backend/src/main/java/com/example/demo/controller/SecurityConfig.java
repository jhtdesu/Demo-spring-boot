package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/oauth2/**").permitAll() // Allow Google callback
                        .anyRequest().authenticated())
                .formLogin(withDefaults())
                .oauth2Login(withDefaults())
                .logout(logout -> logout.permitAll());

        // Enable CORS here as well (alternative to global config, but global is
        // preferred)
        http.cors(withDefaults());

        return http.build();
    }
}