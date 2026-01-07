package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.grpc.AuthzServiceGrpc;
import org.example.grpc.CheckPermissionRequest;
import org.example.grpc.CheckPermissionResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthzmwGrpcClient {

    private final AuthzServiceGrpc.AuthzServiceBlockingStub stub;

    public boolean checkPermission(int userId, String path, String method) {
        try {
            CheckPermissionRequest request = CheckPermissionRequest.newBuilder()
                    .setUserId(userId)
                    .setPath(path)
                    .setMethod(method)
                    .build();

            CheckPermissionResponse response = stub.checkPermission(request);

            boolean allowed = response.getAllowed();
            log.info("AuthzService result for user {} -> {} {} = {}", userId, method, path, allowed);
            return allowed;

        } catch (Exception e) {
            log.error("gRPC call failed for checkPermission: {}", e.getMessage());
            throw new RuntimeException("Authz service unavailable: " + e.getMessage());
        }
    }
}
