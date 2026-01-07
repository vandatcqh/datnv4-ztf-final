package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.jwt.JwtUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final JwtUtil jwtUtil;

    /**
     * Generate access token cho user
     */
    public String generateAccessToken(Integer userId) {
        return jwtUtil.generateToken(userId);
    }

    /**
     * Validate token: chữ ký + expiration + jwt_version
     */
    public boolean validateAccessToken(String token) {
        return jwtUtil.validateTokenFully(token);
    }

    /**
     * Extract user id từ token
     */
    public Integer extractUserId(String token) {
        return jwtUtil.extractUserId(token);
    }
}
