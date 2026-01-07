package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.ApiStatus;
import org.example.dto.UpdateFullnameRequest;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenGrpcClientService tokenGrpcClientService;

    // ====================== GET PROFILE ======================
    public ApiResponse getProfile(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ApiResponse.of(ApiStatus.USER_NOT_FOUND, "User not found");
        }

        User user = optionalUser.get();
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("fullname", user.getFullname());
        data.put("email", user.getEmail());
        data.put("availableFrom", user.getAvailableFrom());
        data.put("createdAt", user.getCreatedAt());

        return ApiResponse.of(ApiStatus.USER_FETCH_SUCCESS, "Profile fetched successfully", data);
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


}
