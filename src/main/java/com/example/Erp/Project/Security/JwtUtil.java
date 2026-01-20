package com.example.Erp.Project.Security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final SecretKey signingKey;
    private static final long EXPIRATION = 86400000; // 1 day

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        if (secret.length() < 32) {
            throw new IllegalArgumentException(
                    "JWT secret must be at least 32 characters long"
            );
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ================= TOKEN GENERATION =================

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(signingKey)
                .compact();
    }

    // ✅ USED BY LOGIN (ROLE INCLUDED)
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(signingKey)
                .compact();
    }

    // ================= CLAIM EXTRACTION =================

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ================= VALIDATION =================

    // 🔹 EXISTING METHOD (USED IF YOU WANT UserDetails CHECK)
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // ✅ REQUIRED BY JwtFilter (THIS FIXES YOUR ERROR)
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ================= INTERNAL =================

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
