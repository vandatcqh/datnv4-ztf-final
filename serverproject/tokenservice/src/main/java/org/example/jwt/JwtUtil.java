package org.example.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT Utility optimized:
 * - Parse token only once per validation
 * - No cache, always query DB for jwt_version
 */
@Component
public class JwtUtil {

    private final Key key;
    private final long expirationTime;
    private final UserRepository userRepository;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expirationTime,
                   UserRepository userRepository) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationTime = expirationTime;
        this.userRepository = userRepository;
    }

    /**
     * Generate JWT token for a given userId
     */
    public String generateToken(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("user_id", user.getId())
                .claim("jwt_version", user.getJwtVersion())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate token fully: signature + expiration + jwt_version
     */
    public boolean validateTokenFully(String token) {
        Claims claims;

        // Step 1: Parse token only once
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }

        // Step 2: Extract claims
        Integer userId = claims.get("user_id", Integer.class);
        Integer tokenVersion = claims.get("jwt_version", Integer.class);

        // Step 3: Query DB for user and check jwt_version
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return tokenVersion.equals(user.getJwtVersion());
    }

    /**
     * Extract userId from token
     */
    public Integer extractUserId(String token) {
        return parseClaims(token).get("user_id", Integer.class);
    }

    /**
     * Extract jwt_version from token
     */
    public Integer extractJwtVersion(String token) {
        return parseClaims(token).get("jwt_version", Integer.class);
    }

    /**
     * Parse claims (single point, still parses token fully)
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
