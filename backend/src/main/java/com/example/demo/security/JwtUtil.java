// package com.example.demo.security;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// import java.nio.charset.StandardCharsets;
// import java.security.Key;
// import java.util.Date;

// @Component
// public class JwtUtil {

//     @Value("${jwt.secret:your-256-bit-secret-your-256-bit-secret}")
//     private String secretKey;

//     @Value("${jwt.expiration:86400000}")
//     private long expirationTime;

//     private Key getSigningKey() {
//         byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
//         return Keys.hmacShaKeyFor(keyBytes);
//     }

//     public String generateToken(String email) {
//         return Jwts.builder()
//                 .setSubject(email)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                 .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                 .compact();
//     }

//     public String extractEmail(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(getSigningKey())
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody()
//                 .getSubject();
//     }

//     public boolean validateToken(String token, String email) {
//         try {
//             String extractedEmail = extractEmail(token);
//             return (extractedEmail.equals(email) && !isTokenExpired(token));
//         } catch (Exception e) {
//             return false;
//         }
//     }

//     private boolean isTokenExpired(String token) {
//         try {
//             Date expiration = Jwts.parserBuilder()
//                     .setSigningKey(getSigningKey())
//                     .build()
//                     .parseClaimsJws(token)
//                     .getBody()
//                     .getExpiration();
//             return expiration.before(new Date());
//         } catch (Exception e) {
//             return true;
//         }
//     }
// }