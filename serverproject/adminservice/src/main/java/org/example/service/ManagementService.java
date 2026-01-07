package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ManagementService {

    private final UserRepository userRepository;
    private final TokenGrpcClientService tokenGrpcClientService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(
//            16,  // salt length
//            32,  // hash length
//            1,   // parallelism
//            65536, // memory cost (KB)
//            3     // iterations
//    );

    // ====================== LIST ======================
    public ApiResponse listUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ApiResponse.of(ApiStatus.USER_NOT_FOUND, "No users found");
        }

        List<Map<String, Object>> userList = new ArrayList<>();
        for (User u : users) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", u.getId());
            userMap.put("username", u.getUsername());
            userMap.put("fullname", u.getFullname());
            userMap.put("email", u.getEmail());
            userMap.put("available", u.isAvailable());
            userMap.put("createdAt", u.getCreatedAt() != null ? u.getCreatedAt().toString() : null);
            userList.add(userMap);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", users.size());
        result.put("users", userList);

        return ApiResponse.of(ApiStatus.USER_LIST_SUCCESS, "Fetched all users successfully", result);
    }

    // ====================== CREATE ======================
    @Transactional
    public ApiResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ApiResponse.of(ApiStatus.USER_CREATE_FAIL, "Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.of(ApiStatus.USER_CREATE_FAIL, "Email already exists");
        }

        User newUser = User.builder()
                .username(request.getUsername())
                .fullname(request.getFullname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .availableFrom(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", newUser.getId());
        data.put("username", newUser.getUsername());
        data.put("email", newUser.getEmail());

        return ApiResponse.of(ApiStatus.USER_CREATE_SUCCESS, "User created successfully", data);
    }

    // ====================== UPDATE FULLNAME ======================
    @Transactional
    public ApiResponse updateFullname(UpdateFullnameRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (!optionalUser.isPresent()) {
            return ApiResponse.of(ApiStatus.USER_NOT_FOUND, "User not found");
        }

        User user = optionalUser.get();
        user.setFullname(request.getFullname());
        userRepository.save(user);

        return ApiResponse.of(ApiStatus.USER_UPDATE_SUCCESS, "Fullname updated successfully");
    }

    // ====================== DELETE USER ======================
    @Transactional
    public ApiResponse deleteUser(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ApiResponse.of(ApiStatus.USER_NOT_FOUND, "User not found");
        }

        User user = optionalUser.get();
        userRepository.delete(user);
        tokenGrpcClientService.revokeAllTokensOfUser(userId);

        return ApiResponse.of(ApiStatus.USER_DELETE_SUCCESS, "User deleted successfully");
    }

    // ====================== LOCK USER ======================
    @Transactional
    public ApiResponse lockUser(LockUserRequest request) {
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (!optionalUser.isPresent()) {
            return ApiResponse.of(ApiStatus.USER_NOT_FOUND, "User not found");
        }

        User user = optionalUser.get();
        LocalDateTime availableFrom = Instant.ofEpochMilli(request.getAvailableFrom())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        user.setAvailableFrom(availableFrom);
        userRepository.save(user);

        tokenGrpcClientService.revokeAllTokensOfUser(request.getUserId());

        return ApiResponse.of(ApiStatus.USER_LOCK_SUCCESS, "User locked until " + availableFrom);
    }
}
