package com.example.ezyshop.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access_token.expiration}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh_token.expiration}")
    private long refreshTokenExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // =========================
    // Generate Access Token
    // =========================

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + accessTokenExpirationMs)
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // Generate Refresh Token
    // =========================

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + refreshTokenExpirationMs)
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // Parse Claims
    // =========================

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // =========================
    // Username
    // =========================

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // =========================
    // Token Type
    // =========================

    public String extractTokenType(String token) {
        return extractAllClaims(token)
                .get("type", String.class);
    }

    // =========================
    // Expiration
    // =========================

    public boolean isTokenExpired(String token) {
        System.out.println(extractAllClaims(token)
                .getExpiration());
        System.out.println(new Date());
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // =========================
    // Access Token
    // =========================

    public boolean isAccessToken(String token) {
        try {
            return "access".equals(extractTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }

    // =========================
    // Refresh Token
    // =========================

    public boolean isRefreshToken(String token) {
        try {
            return "refresh".equals(extractTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }

    // =========================
    // Token Validation
    // =========================

    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {
        try {

            String username = extractUsername(token);

            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);

        } catch (Exception e) {
            return false;
        }
    }
}