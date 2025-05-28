package com.example.demo.config;

import com.example.demo.security.JwtFilter; // Assuming you might use this later
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer; // Import Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration; // Import CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource; // Import CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Import UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import com.example.demo.security.CustomOAuth2UserService;

import java.util.List; // Import List

@Configuration
@EnableWebSecurity
public class SecurityConfig { // Note: Implementing WebMvcConfigurer for CORS here is okay, but defining a
                              // CorsConfigurationSource bean is often preferred when using
                              // http.cors(Customizer.withDefaults())

        @Autowired
        private JwtFilter jwtFilter;

        @Autowired
        private CustomOAuth2UserService customOAuth2UserService;

        // Defining CORS configuration as a bean is generally preferred
        // when using http.cors(Customizer.withDefaults())
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("https://frontend-jh-74d9be1b01e4.herokuapp.com"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(Customizer.withDefaults())
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
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
                                                                "/api/auth/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("https://backend-jh-cff06dd28ef7.herokuapp.com/login")
                                                .defaultSuccessUrl("https://backend-jh-cff06dd28ef7.herokuapp.com/home",
                                                                true)
                                                .failureUrl("https://backend-jh-cff06dd28ef7.herokuapp.com/login")
                                                .permitAll())
                                .oauth2Login(oauth2 -> oauth2
                                                .defaultSuccessUrl("https://backend-jh-cff06dd28ef7.herokuapp.com/home",
                                                                true)
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService)))
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                                                .logoutSuccessUrl(
                                                                "https://backend-jh-cff06dd28ef7.herokuapp.com/login?logout")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll());
                http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                // Explicitly configure session repository (though IF_REQUIRED usually handles
                // this)
                // You don't strictly need this if using default session management
                // .securityContext(securityContext -> {
                // securityContext.securityContextRepository(httpSessionSecurityContextRepository());
                // securityContext.requireExplicitSave(false);
                // });

                return http.build();
        }

        // HttpSessionSecurityContextRepository bean - Needed if explicitly configured
        // above
        // @Bean
        // public SecurityContextRepository httpSessionSecurityContextRepository() {
        // return new HttpSessionSecurityContextRepository();
        // }

        // Password encoder bean - Needed for formLogin (if users are stored locally)
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // AuthenticationManager bean - Often needed for formLogin or manual
        // authentication
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        // Remove the WebMvcConfigurer implementation for CORS if using the
        // CorsConfigurationSource bean
        // @Override
        // public void addCorsMappings(CorsRegistry registry) {
        // // Configuration moved to CorsConfigurationSource bean
        // }
}