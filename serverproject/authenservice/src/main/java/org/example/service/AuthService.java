package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.example.dto.*;
import org.example.kafka.OtpProducer;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenGrpcClientService tokenGrpcClientService;
    private final OtpGrpcClientService otpGrpcClientService;
    private final OtpProducer otpProducer;
    private final RedisTemplate<String, String> redisTemplate;
    private final User2FAService user2FAService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(
//            16,  // salt length
//            32,  // hash length
//            4,   // parallelism
//            65536, // memory cost (KB)
//            3     // iterations
//    );
    private static final long PENDING_EXPIRE_MINUTES = 10;

    public ApiResponse login(LoginRequest request) {
        try {
            // Rate limit theo email
            String rateKey = "login:rate:" + request.getUsername();
            Long current = redisTemplate.opsForValue().increment(rateKey);
            if (current == 1) redisTemplate.expire(rateKey, 1, TimeUnit.MINUTES);
            if (current > 5) {
                return ApiResponse.of(ApiStatus.LOGIN_TOO_MANY_REQUESTS, "Too many requests!!!!!");
            }

            // Tìm user theo username
            User user = userRepository.findByUsername(request.getUsername())
                    .orElse(null);
            if (user == null) return ApiResponse.of(ApiStatus.USER_NOT_FOUND, "User not found");

            // Kiểm tra password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ApiResponse.of(ApiStatus.INVALID_PASSWORD, "Invalid credentials");
            }

            // Kiểm tra trạng thái tài khoản
            if (!user.isAvailable()) {
                return ApiResponse.of(ApiStatus.USER_LOCKED, "Account is locked or unavailable");
            }

            // Cập nhật lastActivityAt
            user.setLastActivityAt(LocalDateTime.now());
            userRepository.save(user);

            boolean has2FA = user2FAService.hasEnabled2FA(user.getId());

            String accessToken = tokenGrpcClientService.generateAccessToken(user.getId());
            String refreshToken = tokenGrpcClientService.createRefreshToken(user.getId());

            Map<String, String> redisData = new HashMap<>();
            redisData.put("userId", String.valueOf(user.getId())); // ✅ convert to String
            redisData.put("username", user.getUsername());
            redisData.put("accessToken", accessToken);
            redisData.put("refreshToken", refreshToken);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("userId", user.getId());
            responseData.put("username", user.getUsername());
            responseData.put("accessToken", accessToken);
            responseData.put("refreshToken", refreshToken);
            responseData.put("transaction2fa_id", null);

            if (!has2FA) {
                return ApiResponse.of(ApiStatus.LOGIN_SUCCESS, "Login successful", responseData);
            }

            // Nếu 2FA enabled → tạo transaction tạm
            String transaction2faId = UUID.randomUUID().toString();
            String pendingKey = "login:2fa:" + transaction2faId;

            redisTemplate.opsForHash().putAll(pendingKey, redisData); // ✅ all String
            redisTemplate.expire(pendingKey, PENDING_EXPIRE_MINUTES, TimeUnit.MINUTES);

            Map<String, Object> respData = new HashMap<>();
            respData.put("transaction2fa_id", transaction2faId);
            respData.put("message", "2FA required");

            return ApiResponse.of(ApiStatus.TWO_FA_REQUIRED, "Please verify 2FA", respData);

        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.LOGIN_FAIL, e.getMessage());
        }
    }

    @Transactional
    public ApiResponse loginVerify2FA(LoginVerify2FARequest request) {
        try {
            String pendingKey = "login:2fa:" + request.getTransaction2faId();
            Map<Object, Object> pendingData = redisTemplate.opsForHash().entries(pendingKey);

            if (pendingData == null || pendingData.isEmpty()) {
                return ApiResponse.of(ApiStatus.TWO_FA_VERIFY_FAIL, "Transaction expired or not found");
            }

            // ✅ parse String về int
            int userId = Integer.parseInt((String) pendingData.get("userId"));

            boolean valid = user2FAService.validateOTP(userId, request.getOtp());
            if (!valid) {
                return ApiResponse.of(ApiStatus.TWO_FA_VERIFY_FAIL, "Invalid OTP");
            }

            // OTP hợp lệ → tạo token và xóa transaction tạm
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String accessToken = tokenGrpcClientService.generateAccessToken(user.getId());
            String refreshToken = tokenGrpcClientService.createRefreshToken(user.getId());

            redisTemplate.delete(pendingKey);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getId());
            data.put("username", user.getUsername());
            data.put("accessToken", accessToken);
            data.put("refreshToken", refreshToken);

            return ApiResponse.of(ApiStatus.LOGIN_SUCCESS, "Login successful", data);

        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.TWO_FA_VERIFY_FAIL, e.getMessage());
        }
    }


    @Transactional
    public ApiResponse logout(String refreshToken) {
        try {
            if (refreshToken != null && !refreshToken.isEmpty()) {
                tokenGrpcClientService.revokeToken(refreshToken);
            } else {
                return ApiResponse.of(ApiStatus.LOGOUT_FAIL, "Missing refresh token");
            }

            return ApiResponse.of(ApiStatus.LOGOUT_SUCCESS, "Logout successful");
        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.LOGOUT_FAIL, e.getMessage());
        }
    }


    public ApiResponse refreshTokens(int userId, String accessToken, String refreshToken) {
        try {
            var validation = tokenGrpcClientService.validateAccessToken(accessToken);
            boolean accessValid = validation.isValid();
            if (accessValid) {
                Map<String, Object> data = new HashMap<>();
                data.put("accessToken", accessToken);
                data.put("refreshToken", refreshToken);
                return ApiResponse.of(ApiStatus.REFRESH_SUCCESS, "Access token still valid", data);
            }

            boolean refreshValid = tokenGrpcClientService.isValid(refreshToken);
            if (!refreshValid) {
                return ApiResponse.of(ApiStatus.REFRESH_FAIL, "Invalid or expired refresh token");
            }

            User user = userRepository.findById(userId)
                    .orElse(null);
            if (user == null) {
                return ApiResponse.of(ApiStatus.REFRESH_FAIL, "User not found");
            }

            // Cập nhật lastActivityAt
            user.setLastActivityAt(LocalDateTime.now());
            userRepository.save(user);

            String newAccessToken = tokenGrpcClientService.generateAccessToken(user.getId());
            String newRefreshToken = tokenGrpcClientService.rotateRefreshToken(refreshToken, user.getId());

            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", newAccessToken);
            data.put("refreshToken", newRefreshToken);

            return ApiResponse.of(ApiStatus.REFRESH_SUCCESS, "Token refreshed successfully", data);

        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.REFRESH_FAIL, e.getMessage());
        }
    }


    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
        try {
            // Kiểm tra email tồn tại
            if (!userRepository.existsByEmail(request.getEmail())) {
                return ApiResponse.of(ApiStatus.FORGOT_PASSWORD_FAIL, "Email not found");
            }

            // Sinh transaction + OTP
            String transactionId = UUID.randomUUID().toString();
            String otp = otpGrpcClientService.generateOtp("forgot_password", transactionId);

            // Lưu pending vào Redis
            String pendingKey = "forgot_password:data:" + transactionId;
            redisTemplate.opsForHash().put(pendingKey, "email", request.getEmail());
            redisTemplate.expire(pendingKey, PENDING_EXPIRE_MINUTES, TimeUnit.MINUTES);

            // Rate limit theo email
            String rateKey = "forgot_password:rate:" + request.getEmail();
            Long current = redisTemplate.opsForValue().increment(rateKey);
            if (current == 1) redisTemplate.expire(rateKey, 1, TimeUnit.MINUTES);
            if (current > 3) {
                return ApiResponse.of(ApiStatus.FORGOT_PASSWORD_FAIL, "Too many requests, please try again later");
            }

            // Gửi OTP
            otpProducer.sendOtp(request.getEmail(), otp);

            Map<String, Object> data = new HashMap<>();
            data.put("transactionId", transactionId);

            return ApiResponse.of(ApiStatus.FORGOT_PASSWORD_SUCCESS,
                    "OTP sent to email. Please verify to reset password.",
                    data);

        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.FORGOT_PASSWORD_FAIL, e.getMessage());
        }
    }


    @Transactional
    public ApiResponse forgotPasswordVerify(ForgotPasswordVerifyRequest request) {
        try {
            boolean valid = otpGrpcClientService.validateOtp("forgot_password", request.getTransactionId(), request.getOtp());
            if (!valid) {
                return ApiResponse.of(ApiStatus.FORGOT_PASSWORD_VERIFY_FAIL, "Invalid OTP");
            }

            String pendingKey = "forgot_password:data:" + request.getTransactionId();
            Map<Object, Object> forgotData = redisTemplate.opsForHash().entries(pendingKey);
            if (forgotData == null || forgotData.isEmpty()) {
                return ApiResponse.of(ApiStatus.FORGOT_PASSWORD_VERIFY_FAIL, "Transaction expired or not found");
            }

            String email = (String) forgotData.get("email");
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return ApiResponse.of(ApiStatus.FORGOT_PASSWORD_VERIFY_FAIL, "User not found");
            }

            // Cập nhật mật khẩu
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            redisTemplate.delete(pendingKey);

            // Thu hồi toàn bộ token cũ (best effort)
            try {
                tokenGrpcClientService.revokeAllTokensOfUser(user.getId());
            } catch (Exception ex) {
                System.err.println("Error revoking user tokens: " + ex.getMessage());
            }

            return ApiResponse.of(ApiStatus.FORGOT_PASSWORD_VERIFY_SUCCESS, "Password reset successfully");

        } catch (Exception e) {
            return ApiResponse.of(ApiStatus.FORGOT_PASSWORD_VERIFY_FAIL, e.getMessage());
        }
    }

}
