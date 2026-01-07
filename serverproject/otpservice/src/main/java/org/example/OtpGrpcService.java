package org.example;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.OtpService;
import org.example.grpc.*;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class OtpGrpcService extends OtpServiceGrpc.OtpServiceImplBase {

    private final OtpService otpService;

    @Override
    public void generateOtp(GenerateOtpRequest request,
                            StreamObserver<GenerateOtpResponse> responseObserver) {
        try {
            log.info("Received GenerateOtp request - Type: {}, TransactionId: {}",
                    request.getType(), request.getTransactionId());

            String otpCode = otpService.generateOtp(
                    request.getType(),
                    request.getTransactionId(),
                    5 * 60 * 1000
            );

            GenerateOtpResponse response = GenerateOtpResponse.newBuilder()
                    .setOtpCode(otpCode)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error generating OTP", e);
            GenerateOtpResponse response = GenerateOtpResponse.newBuilder()
                    .setOtpCode("")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void validateOtp(ValidateOtpRequest request,
                            StreamObserver<ValidateOtpResponse> responseObserver) {
        try {
            log.info("Received ValidateOtp request - Type: {}, TransactionId: {}",
                    request.getType(), request.getTransactionId());

            boolean isValid = otpService.validateOtp(
                    request.getType(),
                    request.getTransactionId(),
                    request.getOtpCode()
            );

            ValidateOtpResponse response = ValidateOtpResponse.newBuilder()
                    .setValid(isValid)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error validating OTP", e);
            ValidateOtpResponse response = ValidateOtpResponse.newBuilder()
                    .setValid(false)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}