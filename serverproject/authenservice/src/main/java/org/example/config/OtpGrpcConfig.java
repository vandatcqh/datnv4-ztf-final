package org.example.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.OtpServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtpGrpcConfig {

    @Bean
    public ManagedChannel otpChannel() {
        return ManagedChannelBuilder
                .forAddress("otp-service", 9051) // port OTP service
                .usePlaintext()
                .enableRetry()
                .build();
    }

    @Bean
    public OtpServiceGrpc.OtpServiceBlockingStub otpServiceStub(ManagedChannel otpChannel) {
        return OtpServiceGrpc.newBlockingStub(otpChannel);
    }
}
