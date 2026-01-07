package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grpc.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenGrpcClientService {

    private final AccessTokenServiceGrpc.AccessTokenServiceBlockingStub accessTokenStub;

    public ValidateAccessTokenResponse validateAccessToken(String accessToken) {
        try {
            ValidateAccessTokenRequest request = ValidateAccessTokenRequest.newBuilder()
                    .setAccessToken(accessToken)
                    .build();

            return accessTokenStub.validateAccessToken(request);

        } catch (Exception e) {
            log.error("gRPC call failed for validateAccessToken: {}", e.getMessage());
            throw new RuntimeException("Token service unavailable: " + e.getMessage(), e);
        }
    }
}
