package org.example.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.AccessTokenServiceGrpc;
import org.example.grpc.RefreshTokenServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenGrpcConfig {

    @Bean
    public ManagedChannel tokenChannel() {
        return ManagedChannelBuilder
                .forAddress("token-service", 9052) // port Token service
                .usePlaintext()
                .enableRetry()
                .build();
    }

    @Bean
    public AccessTokenServiceGrpc.AccessTokenServiceBlockingStub accessTokenServiceStub(ManagedChannel tokenChannel) {
        return AccessTokenServiceGrpc.newBlockingStub(tokenChannel);
    }

    @Bean
    public RefreshTokenServiceGrpc.RefreshTokenServiceBlockingStub refreshTokenServiceStub(ManagedChannel tokenChannel) {
        return RefreshTokenServiceGrpc.newBlockingStub(tokenChannel);
    }
}
