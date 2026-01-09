package org.example.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.AuthzServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthzGrpcConfig {

    @Bean
    public ManagedChannel authzChannel() {
        return ManagedChannelBuilder
                .forAddress("authzmw-service", 9053)
                .usePlaintext()
                .enableRetry()
                .build();
    }

    @Bean
    public AuthzServiceGrpc.AuthzServiceBlockingStub authzServiceBlockingStub(ManagedChannel authzChannel) {
        return AuthzServiceGrpc.newBlockingStub(authzChannel);
    }
}
