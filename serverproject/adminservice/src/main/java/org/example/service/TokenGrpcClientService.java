package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grpc.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenGrpcClientService {

    private final AccessTokenServiceGrpc.AccessTokenServiceBlockingStub accessTokenStub;
    private final RefreshTokenServiceGrpc.RefreshTokenServiceBlockingStub refreshTokenStub;

    public String generateAccessToken(int user_id) {
        try {
            GenerateAccessTokenRequest request = GenerateAccessTokenRequest.newBuilder()
                    .setUserId(user_id)
                    .build();

            GenerateAccessTokenResponse response = accessTokenStub.generateAccessToken(request);
            return response.getAccessToken();

        } catch (Exception e) {
            log.error("gRPC call failed for generateAccessToken: {}", e.getMessage());
            throw new RuntimeException("Token service unavailable: " + e.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    public static class TokenValidationResult {
        private boolean valid;
        private int userId;
    }

    public TokenValidationResult validateAccessToken(String accessToken) {
        try {
            ValidateAccessTokenRequest request = ValidateAccessTokenRequest.newBuilder()
                    .setAccessToken(accessToken)
                    .build();

            ValidateAccessTokenResponse response = accessTokenStub.validateAccessToken(request);
            return new TokenValidationResult(response.getValid(), response.getUserId());

        } catch (Exception e) {
            log.error("gRPC call failed for validateAccessToken: {}", e.getMessage());
            throw new RuntimeException("Token service unavailable: " + e.getMessage());
        }
    }

    public String createRefreshToken(int userId) {
        try {
            CreateRefreshTokenRequest request = CreateRefreshTokenRequest.newBuilder()
                    .setUserId(userId)
                    .build();

            CreateRefreshTokenResponse response = refreshTokenStub.createRefreshToken(request);
            return response.getRefreshToken();

        } catch (Exception e) {
            log.error("gRPC call failed for createRefreshToken: {}", e.getMessage());
            throw new RuntimeException("Token service unavailable: " + e.getMessage());
        }
    }

    public boolean isValid(String token) {
        try {
            IsValidRequest request = IsValidRequest.newBuilder()
                    .setToken(token)
                    .build();

            IsValidResponse response = refreshTokenStub.isValid(request);
            return response.getValid();

        } catch (Exception e) {
            log.error("gRPC call failed for isValid: {}", e.getMessage());
            throw new RuntimeException("Token service unavailable: " + e.getMessage());
        }
    }

    public String rotateRefreshToken(String oldToken, int userId) {
        try {
            RotateRefreshTokenRequest request = RotateRefreshTokenRequest.newBuilder()
                    .setOldToken(oldToken)
                    .setUserId(userId)
                    .build();

            RotateRefreshTokenResponse response = refreshTokenStub.rotateRefreshToken(request);
            return response.getNewToken();

        } catch (Exception e) {
            log.error("gRPC call failed for rotateRefreshToken: {}", e.getMessage());
            throw new RuntimeException("Token service unavailable: " + e.getMessage());
        }
    }

    public void revokeToken(String token) {
        try {
            RevokeTokenRequest request = RevokeTokenRequest.newBuilder()
                    .setToken(token)
                    .build();

            refreshTokenStub.revokeToken(request);

        } catch (Exception e) {
            log.error("gRPC call failed for revokeToken: {}", e.getMessage());
            throw new RuntimeException("Token service unavailable: " + e.getMessage());
        }
    }
    public int revokeAllTokensOfUser(int userId) {
        try {
            RevokeAllTokensOfUserRequest request = RevokeAllTokensOfUserRequest.newBuilder()
                    .setUserId(userId)
                    .build();

            RevokeAllTokensOfUserResponse response = refreshTokenStub.revokeAllTokensOfUser(request);
            return response.getRevokedCount();

        } catch (Exception e) {
            log.error("gRPC call failed for revokeAllTokensOfUser: {}", e.getMessage());
            throw new RuntimeException("Token service unavailable: " + e.getMessage());
        }
    }

}
