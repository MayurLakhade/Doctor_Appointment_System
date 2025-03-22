package com.user.util;

import java.util.Date;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    
    private static final String SECRET_KEY = "pZ3Yxk2eP5sMqv6uZ7BqN3YZd8Yr2PiQjVkLg8X2X9I="; // Min 32 chars

    // Convert secret string to a valid HMAC key
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    //Generate JWT Token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //Validate and Parse JWT Token
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if token is valid
    public static boolean isTokenValid(String token) {
        try {
            parseToken(token); // If parsing succeeds, token is valid
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Invalid token
        }
    }

    //Extract Username from Token
    public static String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    //Extract Issued At (iat) Time
    public static Instant extractIssuedAt(String token) {
        return parseToken(token).getIssuedAt().toInstant();
    }

     //Validate Token with `tokenValidity`
     public static boolean isTokenValid(String token, Instant tokenValidity) {
        try {
            Instant issuedAt = extractIssuedAt(token);
            return !issuedAt.isBefore(tokenValidity); // Ensure `iat` >= `tokenValidity`
        } catch (Exception e) {
            return false;
        }
    }

    // Validate Token against `UserDetails`
    public static boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    //Check if Token is Expired
    public static boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }    
}
