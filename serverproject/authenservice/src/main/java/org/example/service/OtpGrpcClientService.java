package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grpc.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpGrpcClientService {

    private final OtpServiceGrpc.OtpServiceBlockingStub stub;

    public String generateOtp(String type, String transactionId) {
        try {
            GenerateOtpRequest request = GenerateOtpRequest.newBuilder()
                    .setType(type)
                    .setTransactionId(transactionId)
                    .setExpiryMinutes(5)
                    .build();

            GenerateOtpResponse response = stub.generateOtp(request);
            return response.getOtpCode();

        } catch (Exception e) {
            log.error("gRPC call failed for generateOtp: {}", e.getMessage());
            throw new RuntimeException("OTP service unavailable: " + e.getMessage());
        }
    }

    public boolean validateOtp(String type, String transactionId, String otpCode) {
        try {
            ValidateOtpRequest request = ValidateOtpRequest.newBuilder()
                    .setType(type)
                    .setTransactionId(transactionId)
                    .setOtpCode(otpCode)
                    .build();

            ValidateOtpResponse response = stub.validateOtp(request);
            return response.getValid();

        } catch (Exception e) {
            log.error("gRPC call failed for validateOtp: {}", e.getMessage());
            throw new RuntimeException("OTP service unavailable: " + e.getMessage());
        }
    }
}
