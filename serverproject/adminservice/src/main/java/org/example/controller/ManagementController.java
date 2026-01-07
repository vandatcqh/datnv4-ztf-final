package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.example.dto.*;
import org.example.service.AuthzmwGrpcClient;
import org.example.service.ManagementService;
import org.example.service.TokenGrpcClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementService managementService;
    private final TokenGrpcClientService tokenService;
    private final AuthzmwGrpcClient authzClient;

    private boolean authorize(int userId, String path, String method) {
        return authzClient.checkPermission(userId, path, method);
    }

    // ========================== LIST USERS ==========================
    @GetMapping("/allusers")
    public ResponseEntity<ApiResponse> listUsers(
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        var validation = tokenService.validateAccessToken(token);
        if (!validation.isValid()) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.of(ApiStatus.UNAUTHORIZED, "Invalid or expired token"));
        }

        int operatorId = validation.getUserId();
        if (!authorize(operatorId, "/management/allusers", "GET")) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.of(ApiStatus.FORBIDDEN, "Insufficient permissions"));
        }

        ApiResponse response = managementService.listUsers();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // ========================== CREATE USER ==========================
    @PostMapping("/create_user")
    public ResponseEntity<ApiResponse> createUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CreateUserRequest request) {

        String token = authorizationHeader.replace("Bearer ", "");
        var validation = tokenService.validateAccessToken(token);
        if (!validation.isValid()) {
            return ResponseEntity.status(401).body(ApiResponse.of(ApiStatus.UNAUTHORIZED, "Invalid or expired token"));
        }

        int operatorId = validation.getUserId();
        if (!authorize(operatorId, "/management/create_user", "POST")) {
            return ResponseEntity.status(403).body(ApiResponse.of(ApiStatus.FORBIDDEN, "Insufficient permissions"));
        }

        ApiResponse response = managementService.createUser(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // ========================== UPDATE FULLNAME ==========================
    @PutMapping("/update_fullname")
    public ResponseEntity<ApiResponse> updateFullname(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UpdateFullnameRequest request) {

        String token = authorizationHeader.replace("Bearer ", "");
        var validation = tokenService.validateAccessToken(token);
        if (!validation.isValid()) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.of(ApiStatus.UNAUTHORIZED, "Invalid or expired token"));
        }

        int operatorId = validation.getUserId();
        if (!authorize(operatorId, "/management/update_fullname", "PUT")) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.of(ApiStatus.FORBIDDEN, "Insufficient permissions"));
        }

        ApiResponse response = managementService.updateFullname(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // ========================== DELETE USER ==========================
    @DeleteMapping("/delete_user/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int userId) {

        String token = authorizationHeader.replace("Bearer ", "");
        var validation = tokenService.validateAccessToken(token);   
        if (!validation.isValid()) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.of(ApiStatus.UNAUTHORIZED, "Invalid or expired token"));
        }

        int operatorId = validation.getUserId();
        if (!authorize(operatorId, "/management/delete_user", "DELETE")) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.of(ApiStatus.FORBIDDEN, "Insufficient permissions"));
        }

        ApiResponse response = managementService.deleteUser(userId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // ========================== LOCK USER ==========================
    @PostMapping("/lock_user")
    public ResponseEntity<ApiResponse> lockUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody LockUserRequest request) {

        String token = authorizationHeader.replace("Bearer ", "");
        var validation = tokenService.validateAccessToken(token);
        if (!validation.isValid()) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.of(ApiStatus.UNAUTHORIZED, "Invalid or expired token"));
        }

        int operatorId = validation.getUserId();
        if (!authorize(operatorId, "/management/lock_user", "POST")) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.of(ApiStatus.FORBIDDEN, "Insufficient permissions"));
        }

        ApiResponse response = managementService.lockUser(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
