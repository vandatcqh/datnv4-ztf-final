package org.example.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.service.AccessTokenService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class AccessTokenGrpcService extends AccessTokenServiceGrpc.AccessTokenServiceImplBase {

    private final AccessTokenService accessTokenService;

    @Override
    public void generateAccessToken(GenerateAccessTokenRequest request,
                                    StreamObserver<GenerateAccessTokenResponse> responseObserver) {
        log.info("Received generateAccessToken request for user {}", request.getUserId());

        String token = accessTokenService.generateAccessToken(request.getUserId());
        long expiresAt = System.currentTimeMillis() + 3600_000;

        GenerateAccessTokenResponse response = GenerateAccessTokenResponse.newBuilder()
                .setAccessToken(token)
                .setExpiresAt(expiresAt)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("generateAccessToken response sent for user {}", request.getUserId());
    }

    @Override
    public void validateAccessToken(ValidateAccessTokenRequest request,
                                    StreamObserver<ValidateAccessTokenResponse> responseObserver) {
        log.info("Received validateAccessToken request for token {}", request.getAccessToken());

        boolean valid = accessTokenService.validateAccessToken(request.getAccessToken());
        Integer userId = valid ? accessTokenService.extractUserId(request.getAccessToken()) : 0;

        ValidateAccessTokenResponse response = ValidateAccessTokenResponse.newBuilder()
                .setValid(valid)
                .setUserId(userId != null ? userId : 0)
                .build();
        log.info("UserId {}", userId);
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("validateAccessToken response sent for token {}", request.getAccessToken());
    }
}
