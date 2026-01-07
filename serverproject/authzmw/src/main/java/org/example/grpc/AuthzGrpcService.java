package org.example.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.service.AuthzService;

@GrpcService
@RequiredArgsConstructor
public class AuthzGrpcService extends AuthzServiceGrpc.AuthzServiceImplBase {

    private final AuthzService authzService;

    @Override
    public void checkPermission(CheckPermissionRequest request,
                                StreamObserver<CheckPermissionResponse> responseObserver) {
        boolean allowed = authzService.checkPermission(
                request.getUserId(),
                request.getPath(),
                request.getMethod());

        CheckPermissionResponse response = CheckPermissionResponse.newBuilder()
                .setAllowed(allowed)
                .setMessage(allowed ? "Access granted" : "Access denied")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
