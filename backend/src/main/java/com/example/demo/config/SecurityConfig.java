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

import java.util.List; // Import List

@Configuration
@EnableWebSecurity
public class SecurityConfig { // Note: Implementing WebMvcConfigurer for CORS here is okay, but defining a
                              // CorsConfigurationSource bean is often preferred when using
                              // http.cors(Customizer.withDefaults())

        // Keep JwtFilter autowired if you plan to use it later
        // @Autowired
        // private JwtFilter jwtFilter;

        // Defining CORS configuration as a bean is generally preferred
        // when using http.cors(Customizer.withDefaults())
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                // Make sure this matches your frontend development server URL
                configuration.setAllowedOrigins(List.of("http://localhost:5173"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*")); // Or specific headers like "Authorization",
                                                               // "Content-Type"
                configuration.setAllowCredentials(true); // Crucial for cookies/sessions
                configuration.setMaxAge(3600L); // Optional: How long cache preflight response
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration); // Apply this CORS config to all paths
                return source;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // Configure CORS using the bean defined above
                                .cors(Customizer.withDefaults())
                                // CSRF is disabled - common for SPAs, but ensure you understand implications
                                // If using session cookies AND modifying state (POST/PUT/DELETE), CSRF is
                                // recommended
                                .csrf(csrf -> csrf.disable())
                                // Session management: Use sessions if needed (required for standard oauth2Login
                                // state)
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                                // Define authorization rules
                                .authorizeHttpRequests(auth -> auth
                                                // Permit access to static resources, auth endpoints, error pages etc.
                                                // Make sure paths match your project structure
                                                .requestMatchers(
                                                                "/", // Often needed for root path
                                                                "/index.html", // If serving index.html from backend
                                                                "/static/**", // General static resources folder
                                                                "/*.js", "/*.css", "/*.ico", "/*.png", "/*.svg", // Common
                                                                                                                 // static
                                                                                                                 // file
                                                                                                                 // types
                                                                "/home", // Your specific permitted paths
                                                                "/login", // Still permit access TO the login page
                                                                          // itself
                                                                "/register",
                                                                "/error",
                                                                "/api/auth/**" // Your JWT auth path if you re-enable it
                                                ).permitAll()
                                                // All other requests must be authenticated
                                                .anyRequest().authenticated())
                                // Configure form login if you want to support username/password alongside
                                // OAuth2
                                .formLogin(form -> form
                                                .loginPage("/login") // The page user sees/is redirected to FOR form
                                                                     // login
                                                .defaultSuccessUrl("http://localhost:5173/home", true) // Redirect on
                                                                                                       // successful
                                                                                                       // form login
                                                .failureUrl("http://localhost:5173/login") // Redirect on failed login
                                                .permitAll() // Allow access to the login page itself
                                )
                                // Configure OAuth2 Login
                                .oauth2Login(oauth2 -> oauth2
                                                // ** REMOVED .loginPage("/login") **
                                                // Now Spring Security will redirect directly to the OAuth2 provider
                                                // when an unauthenticated request hits a protected resource.
                                                .defaultSuccessUrl("http://localhost:5173/home", true) // Redirect on
                                                                                                       // successful
                                                                                                       // OAuth2 login
                                // Optionally configure user info endpoint if needed
                                // .userInfoEndpoint(userInfo -> userInfo
                                // .userService(...) // Custom OAuth2 user service
                                // )
                                )
                                // Configure logout
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()) // Return
                                                                                                                     // 200
                                                                                                                     // OK
                                                                                                                     // for
                                                                                                                     // AJAX/API
                                                .logoutSuccessUrl("http://localhost:5173/login?logout") // For browser
                                                                                                        // navigation
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll() // Allow access to the logout URL
                                );
                // Add JWT filter *before* the standard auth filters IF/WHEN you re-enable it
                // http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

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