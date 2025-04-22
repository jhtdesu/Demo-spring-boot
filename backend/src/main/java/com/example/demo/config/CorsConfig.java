// package com.example.demo.config; // Adjust the package name to match your project structure

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class CorsConfig implements WebMvcConfigurer {

//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**") // Apply CORS to all paths in your API
//                 .allowedOrigins("http://localhost:5173") // Allow requests from your frontend origin
//                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
//                 .allowedHeaders("*") // Allow all headers
//                 .allowCredentials(true) // If you need to send cookies or authorization headers
//                 .maxAge(3600); // Cache duration for CORS preflight requests
//     }
// }