package org.example.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.service.RefreshTokenService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class RefreshTokenGrpcService extends RefreshTokenServiceGrpc.RefreshTokenServiceImplBase {

    private final RefreshTokenService refreshTokenService;

    @Override
    public void createRefreshToken(CreateRefreshTokenRequest request,
                                   StreamObserver<CreateRefreshTokenResponse> responseObserver) {
        log.info("Received createRefreshToken request for user {}", request.getUserId());

        String token = refreshTokenService.createRefreshToken(request.getUserId());
        long validUntil = System.currentTimeMillis() + 7 * 24 * 3600_000;

        CreateRefreshTokenResponse response = CreateRefreshTokenResponse.newBuilder()
                .setRefreshToken(token)
                .setValidUntil(validUntil)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("createRefreshToken response sent for user {}", request.getUserId());
    }

    @Override
    public void rotateRefreshToken(RotateRefreshTokenRequest request,
                                   StreamObserver<RotateRefreshTokenResponse> responseObserver) {
        log.info("Received rotateRefreshToken request for user {}", request.getUserId());

        String newToken = refreshTokenService.rotateRefreshToken(request.getOldToken(), request.getUserId());
        long validUntil = System.currentTimeMillis() + 7 * 24 * 3600_000;

        RotateRefreshTokenResponse response = RotateRefreshTokenResponse.newBuilder()
                .setNewToken(newToken)
                .setValidUntil(validUntil)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("rotateRefreshToken response sent for user {}", request.getUserId());
    }

    @Override
    public void revokeToken(RevokeTokenRequest request,
                            StreamObserver<RevokeTokenResponse> responseObserver) {
        log.info("Received revokeToken request for token {}", request.getToken());

        refreshTokenService.revokeToken(request.getToken());

        RevokeTokenResponse response = RevokeTokenResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("revokeToken response sent for token {}", request.getToken());
    }

    @Override
    public void revokeAllTokensOfUser(RevokeAllTokensOfUserRequest request,
                                      StreamObserver<RevokeAllTokensOfUserResponse> responseObserver) {
        log.info("Received revokeAllTokensOfUser request for user {}", request.getUserId());

        refreshTokenService.revokeAllTokensOfUser(request.getUserId());

        RevokeAllTokensOfUserResponse response = RevokeAllTokensOfUserResponse.newBuilder()
                .setRevokedCount(0)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("revokeAllTokensOfUser response sent for user {}", request.getUserId());
    }

    @Override
    public void isValid(IsValidRequest request,
                        StreamObserver<IsValidResponse> responseObserver) {
        log.info("Received isValid request for token {}", request.getToken());

        boolean valid = refreshTokenService.isValid(request.getToken());

        IsValidResponse response = IsValidResponse.newBuilder()
                .setValid(valid)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("isValid response sent for token {}", request.getToken());
    }
}
